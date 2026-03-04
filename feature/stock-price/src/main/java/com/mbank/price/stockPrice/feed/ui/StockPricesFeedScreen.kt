package com.mbank.price.stockPrice.feed.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mbank.price.common.model.connectionStatus.ConnectionStatus
import com.mbank.price.domain.model.stock.Stock
import com.mbank.price.stockPrice.component.PriceText
import com.mbank.price.stockPrice.feed.viewModel.StockPriceFeedViewModel
import com.mbank.price.stockPrice.feed.viewModel.StockPricesUiState
import com.mbank.price.ui.theme.LocalStockPriceExtraColors

@Composable
fun StockPricesFeedScreen(viewModel: StockPriceFeedViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LifecycleStartEffect(Unit) {
        viewModel.start()
        onStopOrDispose {
            viewModel.stop()
        }
    }

    Scaffold(
        topBar = {
            FeedTopBar(
                connectionStatus = uiState.connection,
                isFeedRunning = uiState.isRunning,
                onToggleFeed = viewModel::toggleConnection
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->

        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (val state = uiState.stockPriceState) {
                is StockPricesUiState.Failed -> {}
                StockPricesUiState.Loading -> {}
                is StockPricesUiState.Success -> {
                    StockList(stocks = state.stocks)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedTopBar(
    connectionStatus: ConnectionStatus,
    isFeedRunning: Boolean,
    onToggleFeed: () -> Unit
) {
    val priceColor = LocalStockPriceExtraColors.current
    TopAppBar(
        title = {},
        navigationIcon = {
            Box(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(12.dp)
                    .background(
                        color = when (connectionStatus) {
                            ConnectionStatus.CONNECTED -> priceColor.connected
                            ConnectionStatus.CONNECTING -> priceColor.connecting
                            ConnectionStatus.DISCONNECTED -> priceColor.disconnected
                        },
                        shape = CircleShape
                    )
            )
        },
        actions = {
            TextButton(onClick = onToggleFeed) {
                Text(if (isFeedRunning) "Stop" else "Start")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}

@Composable
fun StockList(modifier: Modifier = Modifier, stocks: List<Stock>) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
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
    onClick: (String) -> Unit
) {

    Card(modifier = Modifier.fillMaxWidth()
        .clickable { onClick(stock.symbol) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stock.symbol,
                style = MaterialTheme.typography.titleMedium
            )
        PriceText(price = stock.price)
        }
    }

}