package com.mbank.price.stockPrice.stockDetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mbank.price.stockPrice.component.PriceText
import com.mbank.price.stockPrice.stockDetail.viewModel.StockDetailViewModel

const val SCREEN_ROUTE_SYMBOL_PARAM = "symbol"

@Composable
fun StockDetailScreen(symbol: String, viewModel: StockDetailViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(symbol) {
        viewModel.observeSymbol(symbol)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        uiState.error?.let { /*ErrorBanner(message = it)*/ }
        uiState.stock?.let { stock ->
            Text(
                text = stock.symbol,
                style = MaterialTheme.typography.headlineMedium
            )
            PriceText(price = stock.price)
            Text(
                text = stock.description,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}