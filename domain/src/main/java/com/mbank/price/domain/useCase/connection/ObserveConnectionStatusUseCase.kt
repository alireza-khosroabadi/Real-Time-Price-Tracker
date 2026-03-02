package com.mbank.price.domain.useCase.connection

import com.mbank.price.domain.model.feed.Connection
import com.mbank.price.domain.repository.StockPriceRepository
import com.mbank.price.common.model.appResult.AppResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveConnectionStatusUseCase @Inject constructor(private val repository: StockPriceRepository) {

    operator fun invoke(): Flow<AppResult<Connection>> = repository.observeConnectionStatus()
}