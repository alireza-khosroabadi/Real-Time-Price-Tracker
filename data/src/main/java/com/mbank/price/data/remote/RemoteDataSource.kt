package com.mbank.price.data.remote

import com.mbank.price.common.model.connectionStatus.ConnectionStatus
import com.mbank.price.data.model.priceUpdate.PriceStreamEvent
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

interface RemoteDataSource {
    fun start(symbols: List<String>)
    fun stop()
    fun close()
    fun observeStockPrices():Flow<PriceStreamEvent>
    fun observeWebSocketRunning(): Flow<Boolean>
    fun observeSocketStatus(): Flow<ConnectionStatus>
}