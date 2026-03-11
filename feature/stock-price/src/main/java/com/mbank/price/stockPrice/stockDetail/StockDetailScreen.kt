package com.mbank.price.stockPrice.stockDetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mbank.price.domain.model.stock.Stock
import com.mbank.price.stockPrice.R
import com.mbank.price.stockPrice.component.PercentageText
import com.mbank.price.stockPrice.component.PriceText
import com.mbank.price.stockPrice.stockDetail.viewModel.StockDetailViewModel
import com.mbank.price.ui.component.ErrorScreen

const val SCREEN_ROUTE_SYMBOL_PARAM = "symbol"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockDetailScreen(viewModel: StockDetailViewModel = hiltViewModel(), onBackClick: ()->Unit) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { viewModel.observeSymbol()}

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.stock?.symbol.orEmpty()) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, null)
                    }
                },
                actions = {
                    uiState.stock?.price?.let { PercentageText(it) }
                }
            )
        }
    ) { padding ->

        when {
            uiState.error != null -> uiState.error?.let { ErrorScreen(it) }
            uiState.stock != null -> Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                uiState.stock?.let {
                    PriceCard(it)

                    HorizontalDivider()

                    DescriptionSection(it.description)
                }
            }
        }
    }
}



@Composable
fun PriceCard(stock: Stock) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {

        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                "Current Price",
                style = MaterialTheme.typography.labelLarge
            )

            Spacer(Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                PriceText(stock.price, showFlash = false)
                Spacer(Modifier.width(8.dp))
            }

            Spacer(Modifier.height(16.dp))

        }
    }
}

@Composable
fun DescriptionSection(description: String) {

    Column {
        Text(
            stringResource(R.string.stockDetailScreen_about),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}