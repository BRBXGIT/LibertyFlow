package com.brbx.common.strings

import com.brbx.domain.network.model.result.RequestException
import com.brbx.domain.network.paging.model.PagingException
import com.brbx.feature.common.R
import com.brbx.ui_compose.common.BrbxText
import com.brbx.ui_compose.common.toBrbxText

fun RequestException.asBrbxText(): BrbxText =
    when (this) {
        RequestException.Conflict -> R.string.request_exception_conflict
        RequestException.TooManyRequests -> R.string.request_exception_many_requests
        RequestException.PayloadTooLarge -> R.string.request_exception_payload_large
        RequestException.ServerError -> R.string.request_exception_server
        RequestException.RequestTimeout -> R.string.request_exception_timeout
        RequestException.Internet -> R.string.request_exception_internet
        RequestException.Unknown -> R.string.request_exception_unknown
        RequestException.IncorrectCredentials -> R.string.request_exception_credentials
        RequestException.Unauthorized -> R.string.request_exception_unauthorized
        RequestException.NoEmailOrPassword -> R.string.request_exception_no_email_or_pwassword
    }.toBrbxText()

fun PagingException.asBrbxText(): BrbxText =
    when (this) {
        is PagingException.Conflict -> R.string.request_exception_conflict
        is PagingException.TooManyRequests -> R.string.request_exception_many_requests
        is PagingException.PayloadTooLarge -> R.string.request_exception_payload_large
        is PagingException.ServerError -> R.string.request_exception_server
        is PagingException.RequestTimeout -> R.string.request_exception_timeout
        is PagingException.Internet -> R.string.request_exception_internet
        is PagingException.Unknown -> R.string.request_exception_unknown
        is PagingException.IncorrectCredentials -> R.string.request_exception_credentials
        is PagingException.Unauthorized -> R.string.request_exception_unauthorized
        is PagingException.NoEmailOrPassword -> R.string.request_exception_no_email_or_pwassword
    }.toBrbxText()