package com.mbank.price.stockPrice.stockPricesFeed.viewModel

import androidx.compose.runtime.Immutable
import com.mbank.price.common.model.appResult.AppError
import com.mbank.price.common.model.connectionStatus.ConnectionStatus
import com.mbank.price.domain.model.stock.Stock

@Immutable
data class ConnectionUiState(
    val connection: ConnectionStatus = ConnectionStatus.DISCONNECTED,
    val isRunning: Boolean = true,
)

@Immutable
sealed interface StockPriceFeedUiState{
    data object Loading: StockPriceFeedUiState
    data class Success(val stocks: List<Stock>): StockPriceFeedUiState
    data class Failed(val error: AppError): StockPriceFeedUiState
}