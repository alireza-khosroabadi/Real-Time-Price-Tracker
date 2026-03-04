package com.mbank.price.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color

val DarkBackground = Color(0xFF0F172A)
val DarkSurface = Color(0xFF1E293B)
val DarkPriceUp = Color(0xFF22F86E)
val DarkPriceDown = Color(0xFFEF2163)
val DarkPriceNeutral = Color(0xFFCBD5E1)

internal val DarkColorScheme = darkColorScheme(
    primary = DarkPriceUp,
    onPrimary = Color.Black,
    background = DarkBackground,
    onBackground = Color.White,
    surface = DarkSurface,
    onSurface = Color.White,
    surfaceVariant = Color(0xFF334155),
    outline = Color(0xFF475569)
)

internal val DarkExtraColors = FeedExtraColors(
    priceUp = DarkPriceUp,
    priceDown = DarkPriceDown,
    priceNeutral = DarkPriceNeutral,
    connected = Color.Green,
    disconnected = Color.Red,
    connecting = Color.Yellow
)
