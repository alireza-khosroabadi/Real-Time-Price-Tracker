package com.mbank.price.domain.model.price

data class StockPrice(
    val currentPrice: Double,
    val priceStatus: PriceStatus,
    val lastChangeAtMillis: Long
)
