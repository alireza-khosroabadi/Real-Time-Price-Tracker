package com.mbank.price.data.model.mapper

import com.mbank.price.data.model.price.PriceDto
import com.mbank.price.data.model.priceUpdate.PriceUpdateDto
import com.mbank.price.domain.model.price.PriceStatus
import com.mbank.price.domain.model.price.StockPrice
import com.mbank.price.domain.model.stock.Stock
import java.math.BigDecimal
import java.math.RoundingMode

fun PriceDto.toDomainModel(lastTimeChange: Long): StockPrice = StockPrice(
    currentPrice = this.currentPrice,
    priceStatus = when{
        currentPrice > previousPrice -> PriceStatus.UP
        currentPrice < previousPrice -> PriceStatus.DOWN
        else -> PriceStatus.NO_CHANGE
    },
    changePercentage = calculatePercentageChange(currentPrice, previousPrice),
    lastChangeAtMillis = lastTimeChange
)

fun PriceUpdateDto.toDomainModel(): Stock = Stock(
    symbol = this.stockDto.symbol,
    description = this.stockDto.description,
    price = this.price.toDomainModel(this.timestampMillis)
)


fun calculatePercentageChange(
    current: BigDecimal,
    previous: BigDecimal
): Double {
    if (previous.compareTo(BigDecimal.ZERO) == 0) return 0.0

    return current
        .subtract(previous)
        .divide(previous, 6, RoundingMode.HALF_UP)
        .multiply(BigDecimal(100))
        .toDouble()
}