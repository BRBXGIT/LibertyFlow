package com.brbx.network.base.client

import io.ktor.client.HttpClient

internal interface ApiClientProvider {

    val client: HttpClient
}