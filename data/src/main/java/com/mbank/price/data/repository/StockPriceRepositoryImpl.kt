package com.mbank.price.data.repository

import com.mbank.price.common.model.annotation.ApplicationScope
import com.mbank.price.common.model.appResult.AppError
import com.mbank.price.common.model.appResult.AppResult
import com.mbank.price.common.model.appResult.AppResult.Error
import com.mbank.price.common.model.appResult.AppResult.Success
import com.mbank.price.common.model.connectionStatus.ConnectionStatus
import com.mbank.price.data.model.mapper.toDomainModel
import com.mbank.price.data.model.priceUpdate.PriceStreamEvent
import com.mbank.price.data.remote.RemoteDataSource
import com.mbank.price.data.stockCatalog.StockCatalog
import com.mbank.price.domain.model.feed.Connection
import com.mbank.price.domain.model.feed.StockPriceFeed
import com.mbank.price.domain.model.stock.Stock
import com.mbank.price.domain.param.StockParam
import com.mbank.price.domain.repository.StockPriceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class StockPriceRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    @param:ApplicationScope private val scope: CoroutineScope
) :
    StockPriceRepository {

    private val stockPriceFeed: StateFlow<AppResult<StockPriceFeed>> = combine(
        flow = remoteDataSource.observeStockPrices(),
        flow2 = remoteDataSource.observeWebSocketRunning(),
        flow3 = remoteDataSource.observeSocketStatus()
    ) { stock, isRunning, connectionStatus ->
        when (stock) {
            is PriceStreamEvent.Error -> Error(stock.error)
            is PriceStreamEvent.Update -> Success(
                StockPriceFeed(
                    stocks = stock.update.values
                        .sortedByDescending { it.price.currentPrice }
                        .map { it.toDomainModel() },
                    connectionStatus = Connection(
                        connectionStatus = connectionStatus,
                        isRunning = isRunning
                    )
                )
            )
        }
    }.stateIn(
        initialValue = Success(
            StockPriceFeed(
                emptyList(),
                connectionStatus = Connection(ConnectionStatus.DISCONNECTED, isRunning = false)
            )
        ),
        started = SharingStarted.WhileSubscribed(5_000),
        scope = scope
    )

    override fun observeStockPrices(): Flow<AppResult<StockPriceFeed>> = stockPriceFeed

    override fun observeConnectionStatus(): Flow<AppResult<Connection>> = combine(
        flow = remoteDataSource.observeSocketStatus(),
        flow2 = remoteDataSource.observeWebSocketRunning()
    ) { connection, isRunning ->
        Success(
            Connection(
                connectionStatus = connection,
                isRunning = isRunning
            )
        )
    }

    override fun observeStock(param: StockParam): Flow<AppResult<Stock>> {
        return remoteDataSource.observeStockPrices()
            .map {
                when (it) {
                    is PriceStreamEvent.Error -> Error(it.error)
                    is PriceStreamEvent.Update -> it.update[param.symbol]?.let {
                        Success(it.toDomainModel())
                    } ?: run {
                        Error(AppError.Unknown("Symbol Not Found !"))
                    }
                }
            }
            .onStart {
                when (val lastState = stockPriceFeed.value) {
                    is Error -> emit(Error(lastState.error))
                    is Success -> { lastState.data.stocks
                            .firstOrNull { it.symbol == param.symbol }
                            ?.let { emit(Success(it)) }
                    }
                }
            }
    }

    override fun startFeed() {
        remoteDataSource.start(
            symbols = StockCatalog.symbols,
            seedPrices = StockCatalog.initialQuotes().mapValues { it.value.price.currentPrice }
        )
    }

    override fun stopFeed() {
        remoteDataSource.stop()
    }

    override fun close() {
        remoteDataSource.close()
    }


}