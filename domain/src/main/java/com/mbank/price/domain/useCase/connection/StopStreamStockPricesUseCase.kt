package com.mbank.price.domain.useCase.connection

import com.mbank.price.domain.repository.StockPriceRepository
import javax.inject.Inject

class StopStreamStockPricesUseCase @Inject constructor(private val repository: StockPriceRepository) {
    operator fun invoke() {repository.stopFeed()}
}