package com.mbank.price.data.model.price

import com.mbank.price.common.model.serializer.BigDecimalSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class PriceDto(
    @SerialName("currentPrice")
    @Serializable(with = BigDecimalSerializer::class)
    val currentPrice: BigDecimal,
    @SerialName("previousPrice")
    @Serializable(with = BigDecimalSerializer::class)
    val previousPrice: BigDecimal,
)
