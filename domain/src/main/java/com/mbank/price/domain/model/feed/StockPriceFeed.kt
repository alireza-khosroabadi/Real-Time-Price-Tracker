package com.mbank.price.domain.model.feed

import com.mbank.price.domain.model.stock.Stock

data class StockPriceFeed(
    val stocks: List<Stock>,
    val connectionStatus: Connection
)
