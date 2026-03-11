package com.mbank.price.data.stockCatalog

import com.mbank.price.data.model.price.PriceDto
import com.mbank.price.data.model.priceUpdate.PriceUpdateDto
import com.mbank.price.data.model.stock.StockDto
import java.math.BigDecimal
import java.util.Locale
import kotlin.random.Random

object StockCatalog {

    val symbols: List<String> = listOf(
        "AAPL", "GOOG", "TSLA", "AMZN", "MSFT",
        "NVDA", "META", "NFLX", "AMD", "INTC",
        "ORCL", "IBM", "CRM", "ADBE", "UBER",
        "SHOP", "PYPL", "SQ", "BABA", "SONY",
        "V", "MA", "JPM", "DIS", "KO"
    )

    private val descriptions: Map<String, String> = mapOf(
        "AAPL" to "Apple Inc. designs consumer electronics and software services.",
        "GOOG" to "Alphabet Inc. operates Google search, ads, and cloud products.",
        "TSLA" to "Tesla manufactures electric vehicles and clean energy systems.",
        "AMZN" to "Amazon runs global e-commerce, cloud, and logistics platforms.",
        "MSFT" to "Microsoft builds productivity software, cloud infrastructure, and AI platforms.",
        "NVDA" to "NVIDIA develops GPUs and accelerated computing platforms.",
        "META" to "Meta builds social platforms and virtual reality ecosystems.",
        "NFLX" to "Netflix provides global streaming entertainment services.",
        "AMD" to "AMD designs CPUs and GPUs for consumer and enterprise markets.",
        "INTC" to "Intel builds semiconductor chips and computing platforms.",
        "ORCL" to "Oracle provides enterprise databases and cloud software.",
        "IBM" to "IBM offers enterprise software, consulting, and hybrid cloud services.",
        "CRM" to "Salesforce provides cloud-based CRM and enterprise tools.",
        "ADBE" to "Adobe develops creative, document, and marketing software.",
        "UBER" to "Uber operates ride-sharing, delivery, and logistics services.",
        "SHOP" to "Shopify provides commerce infrastructure for online retailers.",
        "PYPL" to "PayPal offers digital payments and financial services.",
        "SQ" to "Block builds financial tools for payments and commerce.",
        "BABA" to "Alibaba operates e-commerce and cloud services in global markets.",
        "SONY" to "Sony develops electronics, gaming, and media businesses.",
        "V" to "Visa operates global payment processing networks.",
        "MA" to "Mastercard provides payment technologies and services.",
        "JPM" to "JPMorgan Chase is a global financial services firm.",
        "DIS" to "The Walt Disney Company produces media and entertainment assets.",
        "KO" to "Coca-Cola produces and distributes beverage brands worldwide."
    )

    fun descriptionFor(symbol: String): String {
        return descriptions[symbol] ?: "No description available for $symbol."
    }

    fun initialQuotes(): Map<String, PriceUpdateDto> {
        val random = Random(System.currentTimeMillis())
        return symbols.associateWith { symbol ->
            val price =
                String.format(Locale.US, "%.2f", BigDecimal.valueOf(random.nextDouble(80.0, 850.0)))
                    .toBigDecimal()
            PriceUpdateDto(
                stockDto = StockDto(
                    symbol = symbol,
                    description = descriptionFor(symbol)
                ),
                price= PriceDto(
                    currentPrice = price,
                    previousPrice = BigDecimal.ZERO,
                ),
                timestampMillis = System.currentTimeMillis()
            )
        }
    }
}