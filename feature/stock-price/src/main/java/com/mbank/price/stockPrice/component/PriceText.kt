package com.mbank.price.stockPrice.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mbank.price.domain.model.price.PriceStatus
import com.mbank.price.domain.model.price.StockPrice
import com.mbank.price.ui.theme.LocalStockPriceExtraColors
import kotlinx.coroutines.delay
import java.util.Locale

@Composable
fun PriceText(
    price: StockPrice,
    modifier: Modifier = Modifier,
    showFlash: Boolean = true
) {
    val localColor = LocalStockPriceExtraColors.current
    val now by produceState(initialValue = System.currentTimeMillis()) {
        while (true) {
            value = System.currentTimeMillis()
            delay(200)
        }
    }

    val arrow = when (price.priceStatus) {
        PriceStatus.UP -> "↑"
        PriceStatus.DOWN -> "↓"
        PriceStatus.NO_CHANGE -> "→"
    }

    val color = when (price.priceStatus) {
        PriceStatus.UP -> localColor.priceUp
        PriceStatus.DOWN -> localColor.priceDown
        PriceStatus.NO_CHANGE -> localColor.priceNeutral
    }

    val flashColor = when (price.priceStatus) {
        PriceStatus.UP -> localColor.priceUp
        PriceStatus.DOWN -> localColor.priceDown
        PriceStatus.NO_CHANGE -> Color.Transparent
    }

    val isFlashing = showFlash && price.priceStatus != PriceStatus.NO_CHANGE && now - price.lastChangeAtMillis <= 1_000

    Text(
        text = String.format(Locale.US, "$%.2f %s", price.currentPrice, arrow),
        color = color,
        modifier = modifier
            .background(if (isFlashing) flashColor.copy(alpha = 0.1f) else Color.Transparent)
            .clip(shape = MaterialTheme.shapes.small)
            .padding(horizontal = 6.dp, vertical = 2.dp)
    )
}