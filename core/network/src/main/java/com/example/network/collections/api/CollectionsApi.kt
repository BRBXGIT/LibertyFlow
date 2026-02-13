package com.example.network.collections.api

import com.example.network.collections.models.ids.CollectionsIdsDto
import com.example.network.collections.models.request.CollectionRequestDto
import com.example.network.common.common_pagination_models.anime_items_pagination.AnimeItemsPaginationDto
import com.example.network.common.common_request_models.common_request.CommonRequestDtoWithCollectionTypeDto
import com.example.network.common.common_utils.CommonNetworkConstants.AUTHORIZATION_HEADER
import com.example.network.common.common_utils.CommonNetworkConstants.DELETE_METHOD
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Retrofit interface for managing user-specific anime collections.
 * Handles fetching, adding, and removing titles from a user's library.
 */
interface CollectionsApi {

    /**
     * Retrieves a paginated list of anime titles within a specific user collection.
     *
     * @param sessionToken The 'Authorization' bearer token.
     * @param request Filter parameters including the specific collection type.
     * @return A [Response] containing paginated anime items.
     */
    @POST(COLLECTION_RELEASES_URL)
    suspend fun getAnimeInCollection(
        @Header(AUTHORIZATION_HEADER) sessionToken: String,
        @Body request: CommonRequestDtoWithCollectionTypeDto
    ): Response<AnimeItemsPaginationDto>

    /**
     * Fetches all collection IDs associated with the current user.
     * Use this to check what anime in collection(e.g., 'WATCHING', 'PLANNED') the user has.
     *
     * @param sessionToken The 'Authorization' bearer token.
     * @return A [Response] containing a list of anime IDs in each collection.
     */
    @GET(COLLECTIONS_IDS_URL)
    suspend fun getCollectionsIds(
        @Header(AUTHORIZATION_HEADER) sessionToken: String,
    ): Response<CollectionsIdsDto>

    /**
     * Adds a specific anime title to a user's collection.
     *
     * @param sessionToken The 'Authorization' bearer token.
     * @param request Data containing the release ID and target collection.
     * @return A [Response] with no body (Unit).
     */
    @POST(BASE_COLLECTIONS_URL)
    suspend fun addToCollection(
        @Header(AUTHORIZATION_HEADER) sessionToken: String,
        @Body request: CollectionRequestDto
    ): Response<Unit>

    /**
     * Removes a specific anime title from a user's collection.
     * Uses the @HTTP annotation to support a request body in a DELETE operation.
     *
     * @param sessionToken The 'Authorization' bearer token.
     * @param request Data containing list of release ids and its collections.
     * @return A [Response] with no body (Unit).
     */
    @HTTP(method = DELETE_METHOD, path = BASE_COLLECTIONS_URL, hasBody = true)
    suspend fun deleteFromCollection(
        @Header(AUTHORIZATION_HEADER) sessionToken: String,
        @Body request: CollectionRequestDto
    ): Response<Unit>

    companion object Endpoints {
        private const val BASE_COLLECTIONS_URL = "accounts/users/me/collections"

        const val COLLECTION_RELEASES_URL = "$BASE_COLLECTIONS_URL/releases"

        const val COLLECTIONS_IDS_URL = "$BASE_COLLECTIONS_URL/ids"
    }
}