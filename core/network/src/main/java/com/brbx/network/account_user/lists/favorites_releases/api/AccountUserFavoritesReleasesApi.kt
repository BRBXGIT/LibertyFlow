package com.brbx.network.account_user.lists.favorites_releases.api

import com.brbx.network.base.model.request.Request
import com.brbx.network.base.model.response.paginated.PaginatedAnimeItems
import com.brbx.network.base.model.result.RequestResult

interface AccountUserFavoritesReleasesApi {

    suspend fun getFavorites(request: Request.Simple): RequestResult<PaginatedAnimeItems>
}