package com.mbank.price.stockPrice.stockPricesFeed.viewModel

import app.cash.turbine.test
import com.mbank.price.common.model.appResult.AppError
import com.mbank.price.common.model.appResult.AppResult
import com.mbank.price.common.model.connectionStatus.ConnectionStatus
import com.mbank.price.domain.model.feed.Connection
import com.mbank.price.domain.model.feed.StockPriceFeed
import com.mbank.price.domain.model.price.PriceStatus
import com.mbank.price.domain.model.price.StockPrice
import com.mbank.price.domain.model.stock.Stock
import com.mbank.price.domain.useCase.connection.ObserveConnectionStatusUseCase
import com.mbank.price.domain.useCase.connection.StartStreamStockPricesUseCase
import com.mbank.price.domain.useCase.connection.StopStreamStockPricesUseCase
import com.mbank.price.domain.useCase.stock.ObserveStockPricesUseCase
import com.mbank.price.stockPrice.config.MainDispatcherRule
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.math.BigDecimal

@OptIn(ExperimentalCoroutinesApi::class)
class StockPriceFeedViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `init calls start and updates states from flows`() = runTest {
        val observeStockPrices: ObserveStockPricesUseCase = mock()
        val start: StartStreamStockPricesUseCase = mock()
        val stop: StopStreamStockPricesUseCase = mock()
        val observeConn: ObserveConnectionStatusUseCase = mock()

        val stockFlow = MutableStateFlow<AppResult<StockPriceFeed>>(AppResult.Success(StockPriceFeed(emptyList(),
            Connection(ConnectionStatus.CONNECTING, true))))
        val connFlow = MutableStateFlow<AppResult<Connection>>(AppResult.Success(Connection(
            ConnectionStatus.DISCONNECTED, isRunning = true)))

        whenever(observeStockPrices.invoke()).thenReturn(stockFlow)
        whenever(observeConn.invoke()).thenReturn(connFlow)

        val viewModel = StockPriceFeedViewModel(
            observeStockPricesUseCase = observeStockPrices,
            startStreamStockPricesUseCase = start,
            stopStreamStockPricesUseCase = stop,
            observeConnectionStatusUseCase = observeConn
        )

        verify(start).invoke()

        val stocks = listOf(
            Stock(
                symbol = "AAPL",
                description = "Apple Inc.",
                price = StockPrice(
                    currentPrice = BigDecimal("150.00"),
                    priceStatus = PriceStatus.UP,
                    changePercentage = 1.23,
                    lastChangeAtMillis = 0L
                )
            )
        )

        connFlow.emit(AppResult.Success(Connection(ConnectionStatus.CONNECTED, isRunning = true)))
        stockFlow.emit(
            AppResult.Success(
                StockPriceFeed(
                    stocks = stocks,
                    connectionStatus = Connection(ConnectionStatus.CONNECTED, isRunning = true)
                )
            )
        )

        viewModel.feedUiState.test {
            assert(awaitItem() is StockPriceFeedUiState.Loading)
            assertEquals(
                StockPriceFeedUiState.Success(stocks),
                awaitItem()
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `observe connection when start`() = runTest {
        val observeStockPrices: ObserveStockPricesUseCase = mock()
        val start: StartStreamStockPricesUseCase = mock()
        val stop: StopStreamStockPricesUseCase = mock()
        val observeConn: ObserveConnectionStatusUseCase = mock()

        val stockFlow = MutableStateFlow<AppResult<StockPriceFeed>>(AppResult.Success(StockPriceFeed(emptyList(),
            Connection(ConnectionStatus.CONNECTING, true))))
        val connFlow = MutableStateFlow<AppResult<Connection>>(AppResult.Success(Connection(
            ConnectionStatus.DISCONNECTED, isRunning = true)))

        whenever(observeStockPrices.invoke()).thenReturn(stockFlow)
        whenever(observeConn.invoke()).thenReturn(connFlow)

        val viewModel = StockPriceFeedViewModel(
            observeStockPricesUseCase = observeStockPrices,
            startStreamStockPricesUseCase = start,
            stopStreamStockPricesUseCase = stop,
            observeConnectionStatusUseCase = observeConn
        )

        verify(start).invoke()

        connFlow.emit(AppResult.Success(Connection(ConnectionStatus.CONNECTED, isRunning = true)))

        viewModel.connectionUiState.test {
            assertEquals(
                            ConnectionUiState(
                connection = ConnectionStatus.DISCONNECTED,
                isRunning = true
            ),
                awaitItem()
            )

            assertEquals(
                ConnectionUiState(
                    connection = ConnectionStatus.CONNECTED,
                    isRunning = true
                ),
                awaitItem()
            )
            cancelAndIgnoreRemainingEvents()
        }
    }


    @Test
    fun `observe stock prices error updates feed ui state to Failed`() = runTest {
        val observeStockPrices: ObserveStockPricesUseCase = mock()
        val start: StartStreamStockPricesUseCase = mock()
        val stop: StopStreamStockPricesUseCase = mock()
        val observeConn: ObserveConnectionStatusUseCase = mock()

        whenever(observeStockPrices.invoke()).thenReturn(
            flowOf(AppResult.Error(AppError.Network))
        )
        whenever(observeConn.invoke()).thenReturn(emptyFlow())

        val viewModel = StockPriceFeedViewModel(
            observeStockPricesUseCase = observeStockPrices,
            startStreamStockPricesUseCase = start,
            stopStreamStockPricesUseCase = stop,
            observeConnectionStatusUseCase = observeConn
        )

        viewModel.feedUiState.test {
            assertEquals(StockPriceFeedUiState.Loading, awaitItem())
            assertEquals(
                StockPriceFeedUiState.Failed(AppError.Network),
                awaitItem()
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `toggleConnection stops when running`() = runTest {
        val observeStockPrices: ObserveStockPricesUseCase = mock()
        val start: StartStreamStockPricesUseCase = mock()
        val stop: StopStreamStockPricesUseCase = mock()
        val observeConn: ObserveConnectionStatusUseCase = mock()

        val stockFlow = MutableSharedFlow<AppResult<StockPriceFeed>>(replay = 0)
        val connFlow = MutableSharedFlow<AppResult<Connection>>(replay = 0)

        whenever(observeStockPrices.invoke()).thenReturn(stockFlow)
        whenever(observeConn.invoke()).thenReturn(connFlow)

        val viewModel = StockPriceFeedViewModel(
            observeStockPricesUseCase = observeStockPrices,
            startStreamStockPricesUseCase = start,
            stopStreamStockPricesUseCase = stop,
            observeConnectionStatusUseCase = observeConn
        )

        connFlow.emit(AppResult.Success(Connection(ConnectionStatus.CONNECTED, isRunning = true)))
        advanceUntilIdle()

        viewModel.toggleConnection()

        verify(stop).invoke()
    }

    @Test
    fun `toggleConnection starts when not running`() = runTest {
        val observeStockPrices: ObserveStockPricesUseCase = mock()
        val start: StartStreamStockPricesUseCase = mock()
        val stop: StopStreamStockPricesUseCase = mock()
        val observeConn: ObserveConnectionStatusUseCase = mock()

        val stockFlow = MutableSharedFlow<AppResult<StockPriceFeed>>(replay = 0)
        val connFlow = MutableSharedFlow<AppResult<Connection>>(replay = 0)

        whenever(observeStockPrices.invoke()).thenReturn(stockFlow)
        whenever(observeConn.invoke()).thenReturn(connFlow)

        val viewModel = StockPriceFeedViewModel(
            observeStockPricesUseCase = observeStockPrices,
            startStreamStockPricesUseCase = start,
            stopStreamStockPricesUseCase = stop,
            observeConnectionStatusUseCase = observeConn
        )

        connFlow.emit(AppResult.Success(Connection(ConnectionStatus.DISCONNECTED, isRunning = false)))
        advanceUntilIdle()

        viewModel.toggleConnection()

        verify(start).invoke()
    }

    @Test
    fun `connection observe error sets feed Failed`() = runTest {
        val observeStockPrices: ObserveStockPricesUseCase = mock()
        val start: StartStreamStockPricesUseCase = mock()
        val stop: StopStreamStockPricesUseCase = mock()
        val observeConn: ObserveConnectionStatusUseCase = mock()

        whenever(observeStockPrices.invoke()).thenReturn(emptyFlow())
        whenever(observeConn.invoke()).thenReturn(
            flowOf(AppResult.Error(AppError.Network))
        )

        val viewModel = StockPriceFeedViewModel(
            observeStockPricesUseCase = observeStockPrices,
            startStreamStockPricesUseCase = start,
            stopStreamStockPricesUseCase = stop,
            observeConnectionStatusUseCase = observeConn
        )

        viewModel.feedUiState.test {
            assertEquals(StockPriceFeedUiState.Loading, awaitItem())
            assertEquals(
                StockPriceFeedUiState.Failed(AppError.Network),
                awaitItem()
            )
            cancelAndIgnoreRemainingEvents()
        }
    }
}