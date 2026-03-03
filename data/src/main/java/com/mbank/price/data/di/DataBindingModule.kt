package com.mbank.price.data.di

import com.mbank.price.data.repository.StockPriceRepositoryImpl
import com.mbank.price.domain.repository.StockPriceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModuleBinding {

    @Binds
    @Singleton
    abstract fun bindStockPriceRepository(
        repository: StockPriceRepositoryImpl
    ): StockPriceRepository

}