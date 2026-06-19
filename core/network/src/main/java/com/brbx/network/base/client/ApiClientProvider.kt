package com.brbx.network.base.client

import io.ktor.client.HttpClient

interface ApiClientProvider {

    val client: HttpClient
}