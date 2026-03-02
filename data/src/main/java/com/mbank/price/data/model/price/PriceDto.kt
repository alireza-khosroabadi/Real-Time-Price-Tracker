package com.mbank.price.data.model.price

import java.math.BigDecimal

data class PriceDto(
    val currentPrice: BigDecimal,
    val previousPrice: BigDecimal,
)
