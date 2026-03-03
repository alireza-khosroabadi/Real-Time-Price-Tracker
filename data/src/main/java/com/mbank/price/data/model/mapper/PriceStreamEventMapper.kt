package com.mbank.price.data.model.mapper

import com.mbank.price.data.model.price.PriceDto
import com.mbank.price.data.model.priceUpdate.PriceUpdateDto
import com.mbank.price.domain.model.price.PriceStatus
import com.mbank.price.domain.model.price.StockPrice
import com.mbank.price.domain.model.stock.Stock

fun PriceDto.toDomainModel(lastTimeChange: Long): StockPrice = StockPrice(
    currentPrice = this.currentPrice,
    priceStatus = when{
        currentPrice > previousPrice -> PriceStatus.UP
        currentPrice < previousPrice -> PriceStatus.DOWN
        else -> PriceStatus.NO_CHANGE
    },
    lastChangeAtMillis = lastTimeChange
)

fun PriceUpdateDto.toDomainModel(): Stock = Stock(
    symbol = this.stockDto.symbol,
    description = this.stockDto.symbol,
    price = this.price.toDomainModel(this.timestampMillis)
)