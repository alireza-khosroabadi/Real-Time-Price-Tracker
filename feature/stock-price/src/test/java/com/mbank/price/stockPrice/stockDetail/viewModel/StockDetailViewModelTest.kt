package com.mbank.price.stockPrice.stockDetail.viewModel

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.mbank.price.common.model.appResult.AppError
import com.mbank.price.common.model.appResult.AppResult
import com.mbank.price.domain.model.price.PriceStatus
import com.mbank.price.domain.model.price.StockPrice
import com.mbank.price.domain.model.stock.Stock
import com.mbank.price.domain.param.StockParam
import com.mbank.price.domain.useCase.stock.ObserveStockDetailsUseCase
import com.mbank.price.stockPrice.config.MainDispatcherRule
import com.mbank.price.stockPrice.stockDetail.SCREEN_ROUTE_SYMBOL_PARAM
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.math.BigDecimal

@OptIn(ExperimentalCoroutinesApi::class)
class StockDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `observeSymbol success updates uiState`() = runTest {
        val useCase: ObserveStockDetailsUseCase = mock()
        val savedStateHandle = SavedStateHandle(
            mapOf(SCREEN_ROUTE_SYMBOL_PARAM to "AAPL")
        )

        val stock = Stock(
            symbol = "AAPL",
            description = "Apple Inc.",
            price = StockPrice(
                currentPrice = BigDecimal("150.00"),
                priceStatus = PriceStatus.UP,
                changePercentage = 1.23,
                lastChangeAtMillis = 0L
            )
        )

        whenever(useCase.invoke(StockParam("AAPL")))
            .thenReturn(flowOf(AppResult.Success(stock)))

        val viewModel = StockDetailViewModel(
            savedStateHandle = savedStateHandle,
            observeStockDetailsUseCase = useCase
        )

        viewModel.uiState.test {

            assertEquals(StockDetailUiState(), awaitItem())

            viewModel.observeSymbol()

            assertEquals(
                StockDetailUiState(stock = stock, error = null),
                awaitItem()
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `observeSymbol error updates uiState`() = runTest {
        val useCase: ObserveStockDetailsUseCase = mock()
        val savedStateHandle = SavedStateHandle(
            mapOf(SCREEN_ROUTE_SYMBOL_PARAM to "AAPL")
        )

        whenever(useCase.invoke(StockParam("AAPL")))
            .thenReturn(flowOf(AppResult.Error(AppError.Network)))

        val viewModel = StockDetailViewModel(
            savedStateHandle = savedStateHandle,
            observeStockDetailsUseCase = useCase
        )

        viewModel.uiState.test {
            assertEquals(StockDetailUiState(), awaitItem())

            viewModel.observeSymbol()

            assertEquals(
                StockDetailUiState(stock = null, error = AppError.Network),
                awaitItem()
            )

            cancelAndIgnoreRemainingEvents()
        }
    }
}