package com.mbank.price.network.webSocket

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OkHttpWebSocketFactory @Inject constructor(
    private val client: OkHttpClient
) : WebSocketFactory {

    override fun create(
        request: Request,
        listener: WebSocketListener
    ): WebSocket {
        return client.newWebSocket(request, listener)
    }
}