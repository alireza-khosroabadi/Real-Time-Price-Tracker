package com.mbank.price.domain.model.price

data class StockPrice(
    val currentPrice: Double,
    val previousPrice: Double
){
    val priceStatus: PriceStatus = when{
        currentPrice > previousPrice -> PriceStatus.UP
        currentPrice < previousPrice -> PriceStatus.DOWN
        else -> PriceStatus.NO_CHANGE
    }
}
