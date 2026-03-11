package com.mbank.price.data.model.stock

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StockDto(
    @SerialName("symbol") val symbol: String,
    @SerialName("description") val description: String,
    @SerialName("lastChangeAtMillis") val lastChangeAtMillis: Long = System.currentTimeMillis()
)