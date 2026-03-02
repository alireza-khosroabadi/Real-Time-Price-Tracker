package com.mbank.price.domain.useCase.stock

import com.mbank.price.domain.model.feed.StockPriceFeed
import com.mbank.price.domain.repository.StockPriceRepository
import com.mbank.price.model.appResult.AppResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveStockPricesUseCase @Inject constructor(private val repository: StockPriceRepository) {

    operator fun invoke(): Flow<AppResult<StockPriceFeed>> = repository.observeStockPrices()
}