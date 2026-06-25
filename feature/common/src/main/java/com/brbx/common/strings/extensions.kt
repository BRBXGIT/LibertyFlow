package com.brbx.common.strings

import com.brbx.domain.network.model.result.RequestException
import com.brbx.feature.common.R

fun RequestException.asRes(): Int =
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
    }