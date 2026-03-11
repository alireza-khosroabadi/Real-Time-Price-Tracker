package com.mbank.price.domain.repository

import com.mbank.price.common.model.appResult.AppResult
import com.mbank.price.domain.model.feed.Connection
import com.mbank.price.domain.model.feed.StockPriceFeed
import com.mbank.price.domain.model.stock.Stock
import com.mbank.price.domain.param.StockParam
import kotlinx.coroutines.flow.Flow

interface StockPriceRepository {
    fun observeStockPrices(): Flow<AppResult<StockPriceFeed>>
    fun observeConnectionStatus(): Flow<AppResult<Connection>>
    fun observeStock(param: StockParam): Flow<AppResult<Stock>>
    fun startFeed()
    fun stopFeed()
    fun close()
}