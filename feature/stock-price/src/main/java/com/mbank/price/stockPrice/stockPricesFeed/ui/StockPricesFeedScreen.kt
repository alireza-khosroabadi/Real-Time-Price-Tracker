package com.mbank.price.stockPrice.stockPricesFeed.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mbank.price.common.model.connectionStatus.ConnectionStatus
import com.mbank.price.domain.model.stock.Stock
import com.mbank.price.stockPrice.R
import com.mbank.price.stockPrice.component.PriceText
import com.mbank.price.stockPrice.stockPricesFeed.viewModel.StockPriceFeedUiState
import com.mbank.price.stockPrice.stockPricesFeed.viewModel.StockPriceFeedViewModel
import com.mbank.price.ui.component.ErrorScreen
import com.mbank.price.ui.theme.LocalStockPriceExtraColors

@Composable
fun StockPricesFeedScreen(
    viewModel: StockPriceFeedViewModel = hiltViewModel(),
    onItemClick: (symbol: String) -> Unit
) {
    val feedUiState by viewModel.feedUiState.collectAsStateWithLifecycle()
    val connectionUiState by viewModel.connectionUiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
                FeedTopBar(
                    connectionStatus = connectionUiState.connection,
                    isFeedRunning = connectionUiState.isRunning,
                    onToggleFeed = viewModel::toggleConnection
                )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            when (val state = feedUiState) {
                is StockPriceFeedUiState.Failed -> item { ErrorScreen(state.error){viewModel.start()} }
                StockPriceFeedUiState.Loading -> loading(connectionUiState.connection)
                is StockPriceFeedUiState.Success -> stockList(
                    stocks = state.stocks,
                    onItemClick = onItemClick
                )
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
                    .size(16.dp)
                    .background(
                        color = when (connectionStatus) {
                            ConnectionStatus.CONNECTED -> priceColor.connected
                            ConnectionStatus.CONNECTING -> priceColor.connecting
                            ConnectionStatus.DISCONNECTED -> priceColor.disconnected
                        },
                        shape = CircleShape
                    )
            ) {

            }
        },
        actions = {
            TextButton(onClick = onToggleFeed) {
                Text(
                    if (isFeedRunning) stringResource(R.string.stockPricesFeedScreen_connection_stop) else stringResource(
                        R.string.stockPricesFeedScreen_connection_start
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}


fun LazyListScope.loading(connectionStatus: ConnectionStatus) {
    val (iconId, textId) = when (connectionStatus) {
        ConnectionStatus.CONNECTING -> 0 to R.string.connectionStatus_connecting
        ConnectionStatus.CONNECTED -> 0 to R.string.connectionStatus_connected
        ConnectionStatus.DISCONNECTED -> 0 to R.string.connectionStatus_disconnected
    }

    item {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Icon(
                modifier = Modifier.size(50.dp),
                painter = painterResource(iconId),
                contentDescription = stringResource(textId)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(text = stringResource(textId))
        }
    }
}

fun LazyListScope.stockList(stocks: List<Stock>, onItemClick: (symbol: String) -> Unit) {
    item { Spacer(modifier = Modifier.height(8.dp)) }
    items(items = stocks, key = { it.symbol }) { stock ->
        StockRow(
            stock = stock,
            onClick = onItemClick
        )
    }
}

@Composable
fun StockRow(
    stock: Stock,
    onClick: (String) -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
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
            PriceText(price = stock.price)
        }
    }

}