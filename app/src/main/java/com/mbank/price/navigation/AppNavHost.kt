package com.mbank.price.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.mbank.price.stockPrice.stockDetail.SCREEN_ROUTE_SYMBOL_PARAM
import com.mbank.price.stockPrice.stockDetail.StockDetailScreen
import com.mbank.price.stockPrice.stockPricesFeed.ui.StockPricesFeedScreen

@Composable
fun AppNavHost(){
    val navController = rememberNavController()

    NavHost(navController = navController,
        startDestination = StockPriceFeedRoute){
        composable<StockPriceFeedRoute> {
            StockPricesFeedScreen(
                onItemClick = { symbol ->
                    navController.navigate(StockDetailRoute(symbol = symbol))
                }
            )
        }


        composable<StockDetailRoute>(
            deepLinks = listOf(
                navDeepLink { uriPattern = "stocks://details/{$SCREEN_ROUTE_SYMBOL_PARAM}" }
            )
        ) {
            StockDetailScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}