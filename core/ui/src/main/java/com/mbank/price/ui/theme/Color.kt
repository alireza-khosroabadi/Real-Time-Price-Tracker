package com.mbank.price.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class FeedExtraColors(
    val priceUp: Color,
    val priceDown: Color,
    val priceNeutral: Color,
    val connected: Color,
    val disconnected: Color,
    val connecting: Color,
)

val LocalStockPriceExtraColors = staticCompositionLocalOf {
    FeedExtraColors(
        priceUp = LightPriceUp,
        priceDown = LightPriceDown,
        priceNeutral = Color.Gray,
        connected = Color.Green,
        disconnected = Color.Red,
        connecting = Color.Yellow
    )
}