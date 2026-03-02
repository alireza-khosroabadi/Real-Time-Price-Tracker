package com.mbank.price.model.appResult

sealed interface AppError {
    data object Network : AppError
    data object Disconnected : AppError
    data class Unknown(val message: String) : AppError
}