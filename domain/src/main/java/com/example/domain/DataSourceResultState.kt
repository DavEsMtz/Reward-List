package com.example.domain

sealed class DataSourceResultState<out T : Any?> {
    data class Success<out T : Any>(val data: T) : DataSourceResultState<T>()
    data class Error(val error: Throwable) : DataSourceResultState<Nothing>()
}