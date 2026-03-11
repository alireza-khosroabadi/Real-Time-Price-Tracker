package com.mbank.price.stockPrice.stockDetail.viewModel

import androidx.compose.runtime.Immutable
import com.mbank.price.common.model.appResult.AppError
import com.mbank.price.domain.model.stock.Stock

@Immutable
data class StockDetailUiState(
    val stock: Stock? = null,
    val error: AppError? = null
)