package com.brbx.network.base.client

interface ApiClientTokenProvider {

    suspend fun getToken(): String?
    suspend fun clearToken()
}