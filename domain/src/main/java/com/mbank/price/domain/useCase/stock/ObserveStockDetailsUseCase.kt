package com.mbank.price.domain.useCase.stock

import com.mbank.price.common.model.appResult.AppResult
import com.mbank.price.domain.model.stock.Stock
import com.mbank.price.domain.param.StockParam
import com.mbank.price.domain.repository.StockPriceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveStockDetailsUseCase @Inject constructor(private val repository: StockPriceRepository) {

    operator fun invoke(param: StockParam): Flow<AppResult<Stock>> = repository.observeStock(param)

}