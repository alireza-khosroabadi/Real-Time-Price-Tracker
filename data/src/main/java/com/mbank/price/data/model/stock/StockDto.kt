package com.mbank.price.data.model.stock

data class StockDto(
    val symbol: String,
    val description: String,
    val lastChangeAtMillis: Long = System.currentTimeMillis()
)