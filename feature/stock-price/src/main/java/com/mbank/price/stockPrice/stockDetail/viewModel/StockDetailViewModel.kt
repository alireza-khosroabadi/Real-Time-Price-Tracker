package com.mbank.price.stockPrice.stockDetail.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mbank.price.common.model.appResult.AppResult
import com.mbank.price.domain.param.StockParam
import com.mbank.price.domain.useCase.stock.ObserveStockDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class StockDetailViewModel @Inject constructor(
    private val observeStockDetailsUseCase: ObserveStockDetailsUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<StockDetailUiState> = MutableStateFlow(StockDetailUiState())
    val uiState = _uiState.asStateFlow()

    fun observeSymbol(symbol: String) {
        viewModelScope.launch {
            observeStockDetailsUseCase.invoke(
                StockParam(symbol)
            ).collect {
              val state =   when (it) {
                    is AppResult.Error -> StockDetailUiState(error = it.error)
                    is AppResult.Success -> StockDetailUiState(stock = it.data)
                }
                _uiState.value = state
            }
        }
    }
}