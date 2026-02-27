package com.mbank.price.domain.model.feed

import com.mbank.price.model.connectionStatus.ConnectionStatus

data class FeedConnection(
    val connectionStatus: ConnectionStatus,
    val isRunning: Boolean
)
