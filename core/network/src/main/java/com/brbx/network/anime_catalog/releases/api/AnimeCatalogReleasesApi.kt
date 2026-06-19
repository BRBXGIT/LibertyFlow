package com.brbx.network.anime_catalog.releases.api

import com.brbx.network.base.model.request.Request
import com.brbx.network.base.model.response.paginated.PaginatedAnimeItems
import com.brbx.network.base.model.result.RequestResult

interface AnimeCatalogReleasesApi {

    suspend fun getReleases(request: Request.Complex): RequestResult<PaginatedAnimeItems>
}