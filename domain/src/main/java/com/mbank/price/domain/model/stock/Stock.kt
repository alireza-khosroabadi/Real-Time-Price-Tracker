package com.mbank.price.domain.model.stock

import com.mbank.price.domain.model.price.StockPrice

data class Stock(
    val symbol: String,
    val description: String,
    val price: StockPrice
)
