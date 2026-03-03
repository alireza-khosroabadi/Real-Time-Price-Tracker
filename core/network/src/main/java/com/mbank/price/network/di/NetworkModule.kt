package com.mbank.price.network.di

import com.mbank.price.common.model.annotation.ApplicationScope
import com.mbank.price.network.webSocket.OkHttpWebSocketFactory
import com.mbank.price.network.webSocket.WebSocketFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val WS_URL = "wss://ws.postman-echo.com/raw"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder().build()

    @Provides
    @Singleton
    fun provideJson(): Json =
        Json { ignoreUnknownKeys = true }

    @Provides
    @Singleton
    fun provideRequest(): Request =
        Request.Builder().url(WS_URL).build()

    @Provides
    @Singleton
    fun provideWebSocketFactory(
        factory: OkHttpWebSocketFactory
    ): WebSocketFactory = factory

    @Provides
    @Singleton
    @ApplicationScope
    fun provideApplicationScope(): CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.IO)


}