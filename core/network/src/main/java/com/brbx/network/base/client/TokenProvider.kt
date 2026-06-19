package com.brbx.network.base.client

interface TokenProvider {

    fun getToken(): String?
    fun clearToken()
}