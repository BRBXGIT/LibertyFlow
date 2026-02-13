package com.example.network.catalog.api

import com.example.network.common.common_pagination.anime_items_pagination.AnimeItemsPaginationDto
import com.example.network.common.common_request_models.common_request.CommonRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Retrofit interface for fetching anime content from the catalog.
 */
interface CatalogApi {

    /**
     * Fetches a paginated list of anime titles based on provided filters.
     *
     * @param request The filter criteria (e.g., genres, years, types).
     * @return A [Response] containing a paginated list of anime items.
     */
    @POST(RELEASES_URL)
    suspend fun getAnimeByFilters(
        @Body request: CommonRequestDto
    ): Response<AnimeItemsPaginationDto>

    companion object Endpoints {
        const val RELEASES_URL = "anime/catalog/releases"
    }
}