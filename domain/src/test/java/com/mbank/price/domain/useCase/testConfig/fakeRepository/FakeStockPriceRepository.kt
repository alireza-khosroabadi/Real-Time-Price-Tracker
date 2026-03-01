package com.mbank.price.domain.useCase.testConfig.fakeRepository

import com.mbank.price.domain.model.feed.Connection
import com.mbank.price.domain.model.feed.StockPriceFeed
import com.mbank.price.domain.repository.StockPriceRepository
import com.mbank.price.model.appResult.AppResult
import com.mbank.price.model.connectionStatus.ConnectionStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeStockPriceRepository : StockPriceRepository {

    val feedFlow: Flow<AppResult<StockPriceFeed>> = MutableStateFlow(
        AppResult.Success(
            StockPriceFeed(
                stocks = emptyList(),
                connectionStatus = Connection(
                    connectionStatus = ConnectionStatus.CONNECTED,
                    isRunning = true
                )
            )
        )
    )

    override fun observeStockPrices(): Flow<AppResult<StockPriceFeed>> = feedFlow
}