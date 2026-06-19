package com.brbx.network.base.client

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import java.util.concurrent.TimeUnit

internal class ApiClientProviderImpl : ApiClientProvider {

    private companion object {
        const val ConnectTimeout: Long = 30
        const val ReadTimeout: Long = 30
        const val WriteTimeout: Long = 30
        const val ApiUrl: String = "https://aniliberty.top/api/v1/"
    }

    override val client by lazy {
        HttpClient(engineFactory = OkHttp) {
            expectSuccess = true
            engine {
                config {
                    connectTimeout(ConnectTimeout, unit = TimeUnit.SECONDS)
                    readTimeout(ReadTimeout, unit = TimeUnit.SECONDS)
                    writeTimeout(WriteTimeout, unit = TimeUnit.SECONDS)
                }
            }
            install(plugin = DefaultRequest) {
                url(urlString = ApiUrl)
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }
            install(plugin = ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        prettyPrint = true
                        isLenient = true
                    }
                )
            }
            install(plugin = Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL // TODO make NONE in release
            }
        }
    }
}