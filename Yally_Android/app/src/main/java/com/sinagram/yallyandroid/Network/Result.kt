package com.sinagram.yallyandroid.Network

sealed class Result<out T: Any> {
    data class Success<out T : Any>(val data: T, code: Int) : Result<T>()
    data class Error(val exception: String) : Result<Nothing>()
}