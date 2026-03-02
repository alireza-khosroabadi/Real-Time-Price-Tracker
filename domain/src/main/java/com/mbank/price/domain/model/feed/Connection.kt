package com.mbank.price.domain.model.feed

import com.mbank.price.common.model.connectionStatus.ConnectionStatus

data class Connection(
    val connectionStatus: ConnectionStatus,
    val isRunning: Boolean
)
