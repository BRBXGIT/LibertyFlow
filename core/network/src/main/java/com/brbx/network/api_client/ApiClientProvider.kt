package com.brbx.network.api_client

import io.ktor.client.HttpClient

interface ApiClientProvider {

    fun getApiClient(): HttpClient
}