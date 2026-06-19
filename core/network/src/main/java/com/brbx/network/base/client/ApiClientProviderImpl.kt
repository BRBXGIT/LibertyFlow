package com.brbx.network.base.client

import com.brbx.network.account_user.auth.api.AccountUserAuthApiDefaults
import com.brbx.network.anime_catalog.releases.api.AnimeCatalogReleasesApiDefaults
import com.brbx.network.anime_releases.by_id.api.AnimeReleasesByIdDefaults
import com.brbx.network.anime_releases.random.api.AnimeReleasesRandomDefaults
import com.brbx.network.anime_releases.recommened.api.AnimeReleasesRecommendedDefaults
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import java.util.concurrent.TimeUnit

internal class ApiClientProviderImpl(
    private val tokenProvider: TokenProvider,
) : ApiClientProvider {

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
            install(plugin = Auth) {
                bearer {
                    loadTokens {
                        val token = tokenProvider.getToken()
                        token?.let {
                            BearerTokens(
                                accessToken = token,
                                refreshToken = null,
                            )
                        }
                    }
                    refreshTokens {
                        tokenProvider.clearToken()
                        null
                    }
                    sendWithoutRequest { request ->
                        request.url.encodedPath.containsAny(
                            AnimeCatalogReleasesApiDefaults.ReleasesEndPoint,
                            AnimeReleasesByIdDefaults.ByIdEndPoint,
                            AnimeReleasesRandomDefaults.RandomEndPoint,
                            AnimeReleasesRecommendedDefaults.RecommendationsEndPoint,
                            AccountUserAuthApiDefaults.AuthEndPoint,
                        )
                    }
                }
            }
            install(plugin = Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL // TODO make NONE in release
            }
        }
    }

    fun String.containsAny(vararg substrings: String): Boolean {
        return substrings.any { this.contains(it) }
    }
}