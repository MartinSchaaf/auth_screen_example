package com.schaaf.authscreen.presentation

sealed class Response<out  T : Any?> {

    object Loading : Response<Nothing>()

    data class Success<out T : Any?> (val data: T) : Response<T>()

    data class Error(val exception: String) : Response<Nothing>()
}
