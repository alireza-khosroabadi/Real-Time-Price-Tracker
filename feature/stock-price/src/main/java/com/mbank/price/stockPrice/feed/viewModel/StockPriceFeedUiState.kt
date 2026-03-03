package com.mbank.price.stockPrice.feed.viewModel

import androidx.compose.runtime.Immutable
import com.mbank.price.common.model.appResult.AppError
import com.mbank.price.common.model.connectionStatus.ConnectionStatus
import com.mbank.price.domain.model.stock.Stock

@Immutable
data class StockPriceFeedUiState(
    val stockPriceState: StockPricesUiState = StockPricesUiState.Loading,
    val connection: ConnectionStatus = ConnectionStatus.DISCONNECTED,
    val isRunning: Boolean = true,
    val errorMessage: String? = null
)

@Immutable
sealed interface StockPricesUiState{
    data object Loading: StockPricesUiState
    data class Success(val stocks: List<Stock>): StockPricesUiState
    data class Failed(val error: AppError): StockPricesUiState
}