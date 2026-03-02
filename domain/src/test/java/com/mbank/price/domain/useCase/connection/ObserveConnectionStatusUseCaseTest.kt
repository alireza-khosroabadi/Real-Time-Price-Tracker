package com.mbank.price.domain.useCase.connection

import com.mbank.price.domain.useCase.stock.ObserveStockPricesUseCase
import com.mbank.price.domain.useCase.testConfig.fakeRepository.FakeStockPriceRepository
import org.junit.Assert
import org.junit.Test
class ObserveConnectionStatusUseCaseTest {


    @Test
    fun `observe feed connection use case returns repository flow`() {
        val repository = FakeStockPriceRepository()
        val useCase = ObserveConnectionStatusUseCase(repository = repository)
        Assert.assertSame(repository.connectionStatus, useCase.invoke())
    }


}