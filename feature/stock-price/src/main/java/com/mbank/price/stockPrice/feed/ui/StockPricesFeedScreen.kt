package com.mbank.price.stockPrice.feed.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mbank.price.domain.model.stock.Stock
import com.mbank.price.stockPrice.feed.viewModel.StockPriceFeedViewModel
import com.mbank.price.stockPrice.feed.viewModel.StockPricesUiState

@Composable
fun StockPricesFeedScreen(viewModel: StockPriceFeedViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LifecycleStartEffect(Unit) {
        viewModel.start()
        onStopOrDispose {
            viewModel.stop()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        when(val state = uiState.stockPriceState){
            is StockPricesUiState.Failed -> {}
            StockPricesUiState.Loading -> {}
            is StockPricesUiState.Success -> {
                StockList( stocks = state.stocks)
            }
        }
    }
}

@Composable
fun StockList(modifier: Modifier = Modifier, stocks: List<Stock>) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
//        state.errorMessage?.let { message ->
//            item {
//                ErrorBanner(message = message)
//            }
//        }
        items(items = stocks, key = { it.symbol }) { stock ->
            StockRow(
                stock = stock,
                onClick = {}
            )
        }
    }
}

@Composable
fun StockRow(
    stock: Stock,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(stock.symbol) }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stock.symbol,
            style = MaterialTheme.typography.titleMedium
        )
//        PriceText(quote = quote)
        Text(text = stock.price.currentPrice.toString())
    }

    HorizontalDivider()
}