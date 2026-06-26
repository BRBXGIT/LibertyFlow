package com.brbx.domain.network.paging.model

sealed class PagingException : Exception() {
    class Conflict : PagingException()
    class TooManyRequests : PagingException()
    class PayloadTooLarge : PagingException()
    class ServerError : PagingException()
    class IncorrectCredentials : PagingException()
    class Unauthorized : PagingException()
    class NoEmailOrPassword : PagingException()
    class RequestTimeout : PagingException()
    class Internet : PagingException()
    class Unknown : PagingException()
}