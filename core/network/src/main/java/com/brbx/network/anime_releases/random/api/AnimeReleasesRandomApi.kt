package com.brbx.network.anime_releases.random.api

import com.brbx.network.anime_releases.random.model.RandomAnime
import com.brbx.network.base.model.result.RequestResult

interface AnimeReleasesRandomApi {

    suspend fun getReleases(limit: Int = Limit): RequestResult<RandomAnime>

    private companion object {

        const val Limit = 1
    }
}