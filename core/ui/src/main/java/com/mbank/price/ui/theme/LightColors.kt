package com.mbank.price.ui.theme

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color


val LightBackground = Color(0xFFF3F7FC)
val LightSurface = Color(0xFFFFFFFF)
val LightPriceUp = Color(0xFF22F86E)
val LightPriceDown = Color(0xFFEF2163)
val LightPriceNeutral = Color(0xFF475569)

internal val LightColorScheme = lightColorScheme(
    primary = LightPriceUp,
    onPrimary = Color.White,
    background = LightBackground,
    onBackground = Color.Black,
    surface = LightSurface,
    onSurface = Color.Black,
    surfaceVariant = Color(0xFFE2E8F0),
    outline = Color(0xFFCBD5E1)
)

internal val LightExtraColors = FeedExtraColors(
    priceUp = LightPriceUp,
    priceDown = LightPriceDown,
    priceNeutral = LightPriceNeutral,
    connected = Color.Green,
    disconnected = Color.Red,
    connecting = Color.Yellow
)