package com.mbank.price.network.webSocket

import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener

interface WebSocketFactory {
    fun create(
        request: Request,
        listener: WebSocketListener
    ): WebSocket
}