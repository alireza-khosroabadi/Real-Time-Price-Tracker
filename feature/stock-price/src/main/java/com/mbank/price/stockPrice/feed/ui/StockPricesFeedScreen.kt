package com.mbank.price.stockPrice.feed.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mbank.price.stockPrice.feed.viewModel.StockPriceFeedViewModel
import com.mbank.price.stockPrice.feed.viewModel.StockPricesUiState

@Composable
fun StockPricesFeedScreen(viewModel: StockPriceFeedViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        when(val state = uiState.stockPriceState){
            is StockPricesUiState.Failed -> {}
            StockPricesUiState.Loading -> {}
            is StockPricesUiState.Success -> {
                state.stocks.forEach {
                    Text(text = it.price.toString())
                }
            }
        }
    }
}