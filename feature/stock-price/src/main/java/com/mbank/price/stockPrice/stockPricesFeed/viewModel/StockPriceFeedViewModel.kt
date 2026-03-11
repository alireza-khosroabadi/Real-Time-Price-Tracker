package com.mbank.price.stockPrice.stockPricesFeed.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mbank.price.common.model.appResult.AppResult
import com.mbank.price.domain.useCase.connection.ObserveConnectionStatusUseCase
import com.mbank.price.domain.useCase.connection.StartStreamStockPricesUseCase
import com.mbank.price.domain.useCase.connection.StopStreamStockPricesUseCase
import com.mbank.price.domain.useCase.stock.ObserveStockPricesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class StockPriceFeedViewModel @Inject constructor(
    private val observeStockPricesUseCase: ObserveStockPricesUseCase,
    private val startStreamStockPricesUseCase: StartStreamStockPricesUseCase,
    private val stopStreamStockPricesUseCase: StopStreamStockPricesUseCase,
    private val observeConnectionStatusUseCase: ObserveConnectionStatusUseCase
) : ViewModel() {
    private val _feedUiState: MutableStateFlow<StockPriceFeedUiState> =
        MutableStateFlow(StockPriceFeedUiState.Loading)
    val feedUiState: StateFlow<StockPriceFeedUiState> = _feedUiState.asStateFlow()

    private val _connectionUiState: MutableStateFlow<ConnectionUiState> =
        MutableStateFlow(ConnectionUiState())
    val connectionUiState: StateFlow<ConnectionUiState> = _connectionUiState.asStateFlow()


    init {
        observeStockPrices()
        observeConnectionStatus()
        start()
    }

    private fun observeStockPrices() {
        viewModelScope.launch {
            observeStockPricesUseCase.invoke()
                .distinctUntilChanged()
                .collect { appResult ->
                    when (appResult) {
                        is AppResult.Error -> _feedUiState.value = StockPriceFeedUiState.Failed(appResult.error)
                        is AppResult.Success -> _feedUiState.value = StockPriceFeedUiState.Success(appResult.data.stocks)
                    }
                }
        }
    }

    private fun observeConnectionStatus() {
        viewModelScope.launch {
            observeConnectionStatusUseCase.invoke()
                .distinctUntilChanged()
                .collect { appResult ->
                    when (appResult) {
                        is AppResult.Error -> _feedUiState.value = StockPriceFeedUiState.Failed(appResult.error)
                        is AppResult.Success -> _connectionUiState.update { old ->
                            old.copy(
                                connection = appResult.data.connectionStatus,
                                isRunning = appResult.data.isRunning
                            )
                        }
                    }
                }
        }
    }

    fun start() {
        startStreamStockPricesUseCase.invoke()
    }

    fun toggleConnection() {
        if (_connectionUiState.value.isRunning) {
            stopStreamStockPricesUseCase.invoke()
        } else {
            startStreamStockPricesUseCase.invoke()
        }
    }
}