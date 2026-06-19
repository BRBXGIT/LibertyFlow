package com.brbx.network.anime_releases.by_id.api

import com.brbx.network.anime_releases.by_id.model.AnimeItemById
import com.brbx.network.base.model.result.RequestResult

interface AnimeReleasesByIdApi {

    suspend fun getRelease(id: Int): RequestResult<AnimeItemById>
}