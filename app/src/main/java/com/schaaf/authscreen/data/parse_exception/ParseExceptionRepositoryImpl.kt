package com.schaaf.authscreen.data.parse_exception

import android.content.Context
import com.schaaf.authscreen.R
import dagger.hilt.android.qualifiers.ApplicationContext
import com.schaaf.authscreen.domain.parse_exception.ParseExceptionRepository
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException
import javax.inject.Inject

class ParseExceptionRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : ParseExceptionRepository {

    override fun parseException(throwable: Throwable): String {
        var message = context.getString(R.string.unknown_error)
        val result = parseNetworkException(throwable)
        if (result != 0) message = context.getString(result)
        return message
    }


    private fun parseNetworkException(throwable: Throwable): Int {
        var messageResId = 0
        if (checkNoInternet(throwable)) {
            messageResId = R.string.no_internet
        } else if (checkNetworkException(throwable)) {
            messageResId = R.string.server_error
        }
        return messageResId
    }


    private fun checkNoInternet(throwable: Throwable): Boolean {
        return throwable is UnknownHostException ||
                throwable is SocketTimeoutException ||
                throwable is ConnectException
    }

    private fun checkNetworkException(throwable: Throwable): Boolean {
        return throwable is UnknownHostException ||
                throwable is TimeoutException ||
                throwable is SocketException ||
                throwable is SocketTimeoutException
    }
}