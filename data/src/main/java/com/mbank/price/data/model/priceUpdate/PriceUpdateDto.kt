package com.mbank.price.data.model.priceUpdate

import com.mbank.price.data.model.price.PriceDto
import com.mbank.price.data.model.stock.StockDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class PriceUpdateDto(
    @SerialName("stock") val stockDto: StockDto,
    @SerialName("price") val price: PriceDto,
    @SerialName("timestampMillis") val timestampMillis: Long
)
