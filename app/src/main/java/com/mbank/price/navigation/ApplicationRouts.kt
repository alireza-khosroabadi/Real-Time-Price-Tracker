package com.mbank.price.navigation

import com.mbank.price.stockPrice.stockDetail.SCREEN_ROUTE_SYMBOL_PARAM
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data object StockPriceFeedRoute

@Serializable
data class StockDetailRoute(
    @SerialName(SCREEN_ROUTE_SYMBOL_PARAM) val symbol: String
)