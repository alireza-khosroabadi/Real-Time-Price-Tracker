package com.mbank.price.data.remote

import com.mbank.price.data.model.priceUpdate.PriceStreamEvent
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

interface RemoteDataSource {
    fun start(symbols: List<String>, seedPrices: Map<String, BigDecimal>)
    fun stop()
    fun close()
    fun observeSocket():Flow<PriceStreamEvent>
}