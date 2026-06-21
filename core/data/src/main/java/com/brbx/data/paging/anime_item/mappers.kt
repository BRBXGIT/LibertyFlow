package com.brbx.data.paging.anime_item

import com.brbx.network.base.model.result.RequestException

internal fun RequestException.toPagingException(): PagingException =
    when (this) {
        RequestException.Conflict -> PagingException.Conflict()
        RequestException.TooManyRequests -> PagingException.TooManyRequests()
        RequestException.PayloadTooLarge -> PagingException.PayloadTooLarge()
        RequestException.ServerError -> PagingException.ServerError()
        RequestException.IncorrectCredentials -> PagingException.IncorrectCredentials()
        RequestException.Unauthorized -> PagingException.Unauthorized()
        RequestException.NoEmailOrPassword -> PagingException.NoEmailOrPassword()
        RequestException.RequestTimeout -> PagingException.RequestTimeout()
        RequestException.Internet -> PagingException.Internet()
        RequestException.Serialization -> PagingException.Serialization()
        RequestException.Unknown -> PagingException.Unknown()
    }