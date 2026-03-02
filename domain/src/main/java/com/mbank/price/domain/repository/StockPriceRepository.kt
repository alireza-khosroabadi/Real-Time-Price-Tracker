package com.mbank.price.domain.repository

import com.mbank.price.domain.model.feed.Connection
import com.mbank.price.domain.model.feed.StockPriceFeed
import com.mbank.price.common.model.appResult.AppResult
import kotlinx.coroutines.flow.Flow

interface StockPriceRepository {
    fun observeStockPrices(): Flow<AppResult<StockPriceFeed>>
    fun observeConnectionStatus(): Flow<AppResult<Connection>>
}