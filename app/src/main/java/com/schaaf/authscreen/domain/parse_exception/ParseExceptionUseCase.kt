package com.schaaf.authscreen.domain.parse_exception

import javax.inject.Inject

interface ParseExceptionUseCase {

    fun parseException(throwable: Throwable): String
}

class ParseExceptionUseCaseImpl @Inject constructor(
    private val parseExceptionDataRepository: ParseExceptionRepository
) : ParseExceptionUseCase {

    override fun parseException(throwable: Throwable): String = parseExceptionDataRepository.parseException(throwable)
}