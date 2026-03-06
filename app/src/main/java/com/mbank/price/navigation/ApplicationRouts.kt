package com.mbank.price.navigation

import androidx.navigation3.runtime.NavKey
import com.mbank.price.stockPrice.stockDetail.SCREEN_ROUTE_SYMBOL_PARAM
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data object StockPriceFeed: NavKey

@Serializable
data class StockDetail(
    @SerialName(SCREEN_ROUTE_SYMBOL_PARAM) val symbol: String
): NavKey