package com.mbank.price.data.remote

import com.mbank.price.data.model.priceUpdate.PriceStreamEvent
import com.mbank.price.data.model.priceUpdate.PriceUpdateDto
import com.mbank.price.common.model.annotation.ApplicationScope
import com.mbank.price.common.model.appResult.AppError
import com.mbank.price.common.model.connectionStatus.ConnectionStatus
import com.mbank.price.network.webSocket.WebSocketFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import kotlin.random.Random

class WebSocketDataSource @Inject constructor(private val webSocketFactory: WebSocketFactory,
                                              private val json: Json,
                                              private val request: Request,
                                              @param:ApplicationScope private val scope: CoroutineScope) :
    RemoteDataSource {
    private var webSocket: WebSocket? = null
    private val mutex = Mutex()
    private var isRunning: AtomicBoolean = AtomicBoolean(false)
    private val _connectionStatus = MutableStateFlow(ConnectionStatus.DISCONNECTED)
    private var emitJob: Job? = null
    private var reconnectJob: Job? = null
    private val trackedSymbols = LinkedHashSet<String>()
    private val currentPrices = linkedMapOf<String, BigDecimal>()
    private val _updates = MutableSharedFlow<PriceStreamEvent>(extraBufferCapacity = 256)

    override fun start(
        symbols: List<String>,
        seedPrices: Map<String, BigDecimal>
    ) {
        scope.launch {
            mutex.withLock {
                trackedSymbols.clear()
                trackedSymbols.addAll(symbols)
                currentPrices.clear()
                currentPrices.putAll(seedPrices)
            }
        }
        isRunning.set(true)
        connect()
        startEmissionLoop()
    }

    override fun stop() {
        isRunning.set(false)
        cancelAndClearEmitJob()
        closeSocket()
    }

    override fun close() {
        isRunning.set(false)
        cancelAndClearEmitJob()
        cancelAndClearReConnectJob()
        closeSocket()
    }

    override fun observeSocket(): Flow<PriceStreamEvent> = _updates.conflate()


    private fun cancelAndClearEmitJob(){
        emitJob?.cancel()
        emitJob = null
    }

    private fun cancelAndClearReConnectJob(){
        reconnectJob?.cancel()
        reconnectJob = null
    }

    private fun connect(){
        if (!isRunning.get()) return
        _connectionStatus.value = ConnectionStatus.CONNECTING
       webSocket = webSocketFactory.create(request = request, listener = createListener())
    }

    private fun createListener(): WebSocketListener = object: WebSocketListener(){
        override fun onOpen(webSocket: WebSocket, response: Response) {
            _connectionStatus.value = ConnectionStatus.CONNECTED
        }
        override fun onMessage(webSocket: WebSocket, text: String   ) {
            handleIncomingMessage(text)
        }
        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            handleIncomingMessage(bytes.utf8())
        }
        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            this@WebSocketDataSource.webSocket = null
            _connectionStatus.value = ConnectionStatus.DISCONNECTED
            if (isRunning.get()) {
                _updates.tryEmit(PriceStreamEvent.Error(AppError.Disconnected))
            }
            scheduleReconnect()
        }
        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            this@WebSocketDataSource.webSocket = null
            _connectionStatus.value = ConnectionStatus.DISCONNECTED
            _updates.tryEmit(PriceStreamEvent.Error(AppError.Network))
            scheduleReconnect()
        }
    }

    private fun scheduleReconnect(){
        if (!isRunning.get() && reconnectJob?.isActive == true) return

        reconnectJob = scope.launch {
            delay(RECONNECT_DELAY_MS)
            connect()
        }
    }

    private fun startEmissionLoop(){
        if (emitJob?.isActive == true) return
        emitJob = scope.launch {
            while (isActive && isRunning.get()){
                if (_connectionStatus.value == ConnectionStatus.CONNECTED){
                    emitPriceBatch()
                }
                delay(EMIT_INTERVAL_MS)
            }
        }
    }

    private suspend fun emitPriceBatch(){
        mutex.withLock {
            trackedSymbols.mapNotNull { symbol ->
                currentPrices[symbol]?.let { symbol to it }
            }
        }.forEach { (symbol, current) ->
            val next = generateNextPrice(current)
            val priceUpdate =PriceUpdateDto(
                symbol = symbol,
                price = next,
                timestampMillis = System.currentTimeMillis()
            )
            webSocket?.send( json.encodeToString(priceUpdate))
        }
    }

    private fun handleIncomingMessage(text: String) {
        val price = runCatching {
            json.decodeFromString<PriceUpdateDto>(text)
        }.getOrNull()

        price?.let { data ->
            scope.launch {
                mutex.withLock {
                    if (!trackedSymbols.contains(data.symbol)) return@launch
                    currentPrices[data.symbol] = data.price
                }
                _updates.emit(PriceStreamEvent.Update(data))
            }
        }?:run {
            _updates.tryEmit(PriceStreamEvent.Error(AppError.Unknown("Failed to parse stream message")))
        }
    }

    private fun generateNextPrice(current: BigDecimal): BigDecimal {
        val movePercent = BigDecimal.valueOf(Random.nextDouble(-0.02, 0.02))
        val next = current.multiply(BigDecimal.ONE.add(movePercent))
        return next.setScale(2, RoundingMode.HALF_UP).coerceAtLeast(BigDecimal.ONE)
    }

    private fun closeSocket() {
        webSocket?.close(1000, "Closed by client")
        webSocket = null
        _connectionStatus.value = ConnectionStatus.DISCONNECTED
    }

    private companion object {
        private const val EMIT_INTERVAL_MS = 2_000L
        private const val RECONNECT_DELAY_MS = 3_000L
    }
}