package com.brbx.network.anime_releases.recommened.api

import com.brbx.network.base.model.response.common.AnimeItem
import com.brbx.network.base.model.result.RequestResult

interface AnimeReleasesRecommendedApi {

    suspend fun getReleases(
        releaseId: Int,
        limit: Int = Limit,
    ): RequestResult<List<AnimeItem>>

    private companion object {

        const val Limit = 10
    }
}