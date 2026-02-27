package com.mbank.price.domain.model.feed

import com.mbank.price.domain.model.stock.Stock

data class StockPriceFeed(
    val stockList: List<Stock>
)
