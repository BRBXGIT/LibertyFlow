package com.example.network.favorites.api

import com.example.network.common.common_pagination_models.anime_items_pagination.AnimeItemsPaginationDto
import com.example.network.common.common_request_models.common_request.CommonRequestDto
import com.example.network.common.common_utils.CommonNetworkConstants.AUTHORIZATION_HEADER
import com.example.network.common.common_utils.CommonNetworkConstants.DELETE_METHOD
import com.example.network.favorites.models.FavoriteIdsResponseDto
import com.example.network.favorites.models.FavoriteRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Retrofit interface for managing a user's favorite anime releases.
 */
interface FavoritesApi {

    /**
     * Retrieves a paginated list of anime titles marked as favorites by the user.
     *
     * @param sessionToken Authentication token (Bearer).
     * @param request Standard pagination and filter parameters.
     * @return A [Response] containing paginated favorite items.
     */
    @POST(FAVORITES_RELEASES_URL)
    suspend fun getFavorites(
        @Header(AUTHORIZATION_HEADER) sessionToken: String,
        @Body request: CommonRequestDto
    ): Response<AnimeItemsPaginationDto>

    /**
     * Fetches a list of IDs for all releases currently in the user's favorites.
     * Useful for showing "heart" icons or active states in lists.
     *
     * @param sessionToken Authentication token (Bearer).
     * @return A [Response] containing favorite identifiers.
     */
    @GET(FAVORITES_IDS_URL)
    suspend fun getFavoritesIds(
        @Header(AUTHORIZATION_HEADER) sessionToken: String
    ): Response<FavoriteIdsResponseDto>

    /**
     * Adds a specific anime release to the user's favorites list.
     *
     * @param sessionToken Authentication token (Bearer).
     * @param request DTO containing the release ID to add.
     * @return A [Response] with no body indicating success or failure.
     */
    @POST(BASE_FAVORITES_URL)
    suspend fun addFavorite(
        @Header(AUTHORIZATION_HEADER) sessionToken: String,
        @Body request: FavoriteRequestDto
    ): Response<Unit>

    /**
     * Removes a specific anime release from the user's favorites list.
     *
     * @param sessionToken Authentication token (Bearer).
     * @param request DTO containing the release ID to remove.
     * @return A [Response] with no body indicating success or failure.
     */
    @HTTP(
        method = DELETE_METHOD,
        path = BASE_FAVORITES_URL,
        hasBody = true
    )
    suspend fun deleteFavorite(
        @Header(AUTHORIZATION_HEADER) sessionToken: String,
        @Body request: FavoriteRequestDto
    ): Response<Unit>

    companion object Endpoints {
        private const val BASE_FAVORITES_URL = "accounts/users/me/favorites"

        const val FAVORITES_RELEASES_URL = "$BASE_FAVORITES_URL/releases"

        const val FAVORITES_IDS_URL = "$BASE_FAVORITES_URL/ids"
    }
}