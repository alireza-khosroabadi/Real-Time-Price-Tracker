package com.mbank.price.data.model.priceUpdate

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class PriceUpdateDto(
    @SerialName("symbol") val symbol: String,
    @SerialName("price") val price: BigDecimal,
    @SerialName("timestampMillis") val timestampMillis: Long
)
