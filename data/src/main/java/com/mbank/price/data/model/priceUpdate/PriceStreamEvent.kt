package com.mbank.price.data.model.priceUpdate

import com.mbank.price.common.model.appResult.AppError

sealed interface PriceStreamEvent {
    data class Update(val update: Set<PriceUpdateDto>) : PriceStreamEvent
    data class Error(val error: AppError) : PriceStreamEvent
}