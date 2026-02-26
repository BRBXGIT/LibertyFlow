package com.example.data

import androidx.paging.PagingSource
import com.example.data.utils.network.network_caller.NetworkCaller
import com.example.data.utils.network.paging.CommonPagingSource
import com.example.network.catalog.api.CatalogApi
import com.example.network.common.common_pagination_models.anime_items_pagination.AnimeItemsPaginationDto
import com.example.network.common.common_pagination_models.meta.LinksDto
import com.example.network.common.common_pagination_models.meta.MetaDto
import com.example.network.common.common_pagination_models.meta.PaginationDto
import com.example.network.common.common_request_models.common_request.CommonRequestDto
import com.example.unit.base.base.BaseUnitTest
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class CommonPagingSourceTest: BaseUnitTest() {

    private val networkCaller = NetworkCaller()
    private val api = mockk<CatalogApi>()
    private val baseRequest = CommonRequestDto(
        page = 1,
        requestParameters = mockk()
    )
    private lateinit var pagingSource: CommonPagingSource

    @Before
    fun setUp() {
        pagingSource = CommonPagingSource(
            networkCaller = networkCaller,
            apiCall = { api.getAnimeByFilters(it as CommonRequestDto) },
            baseRequest = baseRequest
        )
    }

    @Test
    fun `paging returns success when call is successful`() = runTest {
        val fakeResponse = AnimeItemsPaginationDto(
            data = emptyList(),
            metaDto = MetaDto(
                PaginationDto(
                    count = 20,
                    currentPage = 1,
                    linksDto = LinksDto(),
                    perPage = 20,
                    total = 100,
                    totalPages = 5
                )
            )
        )

        coEvery { api.getAnimeByFilters(baseRequest) } returns Response.success(fakeResponse)

        val expectedResult = PagingSource.LoadResult.Page(
            data = fakeResponse.data,
            prevKey = null,
            nextKey = 2
        )

        val actualResult = pagingSource.load(
            params = PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 2,
                placeholdersEnabled = false
            )
        )

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `paging returns error with correct error resId when call is error`() = runTest {
        val errorResponseBody = "{}".toResponseBody("application/json".toMediaTypeOrNull())
        val response = Response.error<AnimeItemsPaginationDto>(606, errorResponseBody)
        coEvery { api.getAnimeByFilters(baseRequest) } returns response

        val actualResult = pagingSource.load(
            params = PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 2,
                placeholdersEnabled = false
            )
        )

        assertTrue(actualResult is PagingSource.LoadResult.Error)
        val actualError = actualResult as PagingSource.LoadResult.Error

        assertEquals(R.string.unknown_message.toString(), actualError.throwable.message)
    }
}