package com.brbx.network.anime_releases.by_id.api

import com.brbx.network.anime_releases.by_id.model.AnimeItemById
import com.brbx.network.base.api.ApiCallExecutor
import com.brbx.network.base.client.ApiClientProvider
import com.brbx.network.base.common.RequestDefaults
import com.brbx.network.base.common.RequestParameters
import com.brbx.network.base.model.result.RequestResult
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

internal class AnimeReleasesByIdApiImpl(
    private val apiCallExecutor: ApiCallExecutor,
    private val apiClientProvider: ApiClientProvider,
) : AnimeReleasesByIdApi {

    override suspend fun getRelease(id: Int): RequestResult<AnimeItemById> =
        apiCallExecutor.execute {
            apiClientProvider.client.get(
                urlString = "${AnimeReleasesByIdDefaults.ByIdEndPoint}ByIdEndPoint/$id",
            ) {
                parameter(RequestParameters.Include, Include)
                parameter(RequestParameters.Exclude, RequestDefaults.Exclude)
            }.body()
        }

    private companion object {

        const val Include = "poster.optimized," +
                "name," +
                "season.description," +
                "type.description," +
                "year," +
                "is_ongoing," +
                "description," +
                "genres.name," +
                "members.role.description,members.nickname," +
                "episodes.opening,episodes.ending,episodes.hls_480,episodes.hls_720,episodes.hls_1080,episodes.name," +
                "torrents.filename,torrents.leechers,torrents.seeders,torrents.size,torrents.magnet," +
                "id"
    }
}