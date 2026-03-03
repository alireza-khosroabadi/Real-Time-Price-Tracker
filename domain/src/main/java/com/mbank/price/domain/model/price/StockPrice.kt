package com.mbank.price.domain.model.price

import java.math.BigDecimal

data class StockPrice(
    val currentPrice: BigDecimal,
    val priceStatus: PriceStatus,
    val lastChangeAtMillis: Long = System.currentTimeMillis()
)
