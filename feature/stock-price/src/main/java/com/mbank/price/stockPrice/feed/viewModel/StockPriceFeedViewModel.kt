package com.mbank.price.stockPrice.feed.viewModel

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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class StockPriceFeedViewModel @Inject constructor(
    private val observeStockPricesUseCase: ObserveStockPricesUseCase,
    private val startStreamStockPricesUseCase: StartStreamStockPricesUseCase,
    private val stopStreamStockPricesUseCase: StopStreamStockPricesUseCase,
    private val observeConnectionStatusUseCase: ObserveConnectionStatusUseCase
) : ViewModel() {
    private val _uiState: MutableStateFlow<StockPriceFeedUiState> = MutableStateFlow(StockPriceFeedUiState())
    val uiState: StateFlow<StockPriceFeedUiState> = _uiState.asStateFlow()

    init {
        observeStockPrices()
        observeConnectionStatus()
    }

    private fun observeStockPrices(){
        viewModelScope.launch {
            observeStockPricesUseCase.invoke().collect { appResult ->
                when(appResult){
                    is AppResult.Error -> _uiState.update { old -> old.copy(
                        stockPriceState = StockPricesUiState.Failed(appResult.error)
                    )}
                    is AppResult.Success -> _uiState.update { old -> old.copy(
                        stockPriceState = StockPricesUiState.Success(appResult.data.stocks),
                        connection = appResult.data.connectionStatus.connectionStatus,
                        isRunning = appResult.data.connectionStatus.isRunning
                    ) }
                }
            }
        }
    }

    fun observeConnectionStatus(){
        viewModelScope.launch {
            observeConnectionStatusUseCase.invoke().collect { appResult ->
                when(appResult){
                    is AppResult.Error -> {}
                    is AppResult.Success -> _uiState.update { old -> old.copy(
                        connection = appResult.data.connectionStatus,
                        isRunning = appResult.data.isRunning
                    )}
                }
            }
        }
    }

    fun start(){
        startStreamStockPricesUseCase.invoke()
    }

    fun stop(){stopStreamStockPricesUseCase.invoke()}

}