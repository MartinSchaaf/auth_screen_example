package com.schaaf.authscreen.domain.parse_exception

interface ParseExceptionRepository {
    fun parseException(throwable: Throwable): String
}
