package com.mbank.price.data.repository

import com.mbank.price.common.model.appResult.AppResult
import com.mbank.price.data.mapper.toDomainModel
import com.mbank.price.data.model.priceUpdate.PriceStreamEvent
import com.mbank.price.data.remote.RemoteDataSource
import com.mbank.price.data.stockCatalog.StockCatalog
import com.mbank.price.domain.model.feed.Connection
import com.mbank.price.domain.model.feed.StockPriceFeed
import com.mbank.price.domain.repository.StockPriceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class StockPriceRepositoryImpl @Inject constructor(private val remoteDataSource: RemoteDataSource) :
    StockPriceRepository {

    private val stockPriceFeed: Flow<AppResult<StockPriceFeed>> = combine(
        flow = remoteDataSource.observeStockPrices(),
        flow2 = remoteDataSource.observeWebSocketRunning(),
        flow3 = remoteDataSource.observeSocketStatus()
    ) { stock, isRunning, connectionStatus ->
        when(stock){
            is PriceStreamEvent.Error -> AppResult.Error(stock.error)
            is PriceStreamEvent.Update -> AppResult.Success(
                StockPriceFeed(
                    stocks = stock.update
                        .sortedByDescending { it.price.currentPrice }
                        .map { it.toDomainModel() },
                    connectionStatus = Connection(
                        connectionStatus = connectionStatus,
                        isRunning = isRunning
                    )
                )
            )
        }
    }

    override fun observeStockPrices(): Flow<AppResult<StockPriceFeed>> = stockPriceFeed

    override fun observeConnectionStatus(): Flow<AppResult<Connection>>  =  combine(
        flow = remoteDataSource.observeSocketStatus(),
        flow2 = remoteDataSource.observeWebSocketRunning()
    ){ connection, isRunning -> AppResult.Success(Connection(connectionStatus = connection, isRunning = isRunning)) }

    override fun startFeed() {
        remoteDataSource.start(
            symbols = StockCatalog.symbols,
        )
    }

    override fun stopFeed() {
        remoteDataSource.stop()
    }

    override fun close() {
        remoteDataSource.close()
    }


}