package com.mbank.price.domain.useCase.stock

import com.mbank.price.domain.useCase.testConfig.fakeRepository.FakeStockPriceRepository
import org.junit.Assert
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class ObserveStockPricesUseCaseTest {

    @Test
    fun `observe prices use case returns repository flow`(){
        val repository = FakeStockPriceRepository()
        val useCase = ObserveStockPricesUseCase(repository = repository)
        assertSame(repository.feedFlow , useCase.invoke())
    }
}