package com.brbx.network.account_user.lists.collections.collections_releases.api

import com.brbx.network.base.model.request.Request
import com.brbx.network.base.model.response.paginated.PaginatedAnimeItems
import com.brbx.network.base.model.result.RequestResult

interface AccountUserCollectionsReleasesApi {

    suspend fun getCollection(request: Request.Collection): RequestResult<PaginatedAnimeItems>
}