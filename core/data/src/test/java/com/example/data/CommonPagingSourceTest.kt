package com.example.data

import androidx.paging.PagingSource
import com.example.data.utils.network.paging.CommonPagingSource
import com.example.network.catalog.api.CatalogApi
import com.example.network.common.common_pagination_models.anime_items_pagination.AnimeItemsPaginationDto
import com.example.network.common.common_pagination_models.meta.LinksDto
import com.example.network.common.common_pagination_models.meta.MetaDto
import com.example.network.common.common_pagination_models.meta.PaginationDto
import com.example.network.common.common_request_models.common_request.CommonRequestDto
import com.example.network.common.common_request_models.common_request_base.CommonRequestDtoBase
import com.example.network.common.common_response_models.AnimeResponseItemDto
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class CommonPagingSourceTest {

    private val api = mockk<CatalogApi>()
    private val baseRequest = mockk<CommonRequestDtoBase>()
    private lateinit var pagingSource: CommonPagingSource

    @Before
    fun setUp() {
        pagingSource = CommonPagingSource(
            apiCall = { api.getAnimeByFilters(baseRequest as CommonRequestDto) },
            baseRequest = baseRequest
        )
    }

    @Test
    fun `load returns success on valid api call`() = runTest {
        val items = listOf(mockk<AnimeResponseItemDto>())
        val paginationDto = PaginationDto(
            count = 20,
            currentPage = 1,
            linksDto = LinksDto(next = null),
            perPage = 20,
            total = 40,
            totalPages = 2
        )
        val response = AnimeItemsPaginationDto(
            data = items,
            metaDto = MetaDto(paginationDto)
        )

        coEvery { api.getAnimeByFilters(any()) } returns Response.success(response)
        every { baseRequest.withPageAndLimit(any()) } returns baseRequest

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 1,
                loadSize = 10,
                placeholdersEnabled = false
            )
        )

        val expected = PagingSource.LoadResult.Page(
            data = items,
            prevKey = null,
            nextKey = 2
        )

        assertEquals(expected, result)
    }
}