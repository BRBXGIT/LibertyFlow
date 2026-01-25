package com.example.data

import androidx.paging.PagingSource
import com.example.data.utils.remote.paging.CommonPagingSource
import com.example.network.common.common_pagination.anime_items_pagination.AnimeItemsPaginationDto
import com.example.network.common.common_pagination.meta.LinksDto
import com.example.network.common.common_pagination.meta.MetaDto
import com.example.network.common.common_pagination.meta.PaginationDto
import com.example.network.common.common_request_models.common_request.CommonRequestDto
import com.example.network.common.common_response_models.AnimeResponseItemDto
import com.example.network.favorites.api.FavoritesApi
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class CommonPagingSourceTest {

    private val api: FavoritesApi = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var pagingSource: CommonPagingSource

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `load returns Page on success`() = runTest {
        val fakeItems = listOf(
            mockk<AnimeResponseItemDto>(),
            mockk<AnimeResponseItemDto>()
        )
        val fakeResponse = Response.success(
            AnimeItemsPaginationDto(
                data = fakeItems,
                metaDto = MetaDto(
                    paginationDto = PaginationDto(
                        count = 2,
                        currentPage = 1,
                        linksDto = LinksDto(""),
                        perPage = 20,
                        totalPages = 15,
                        total = 300,
                    )
                )
            )
        )

        val fakeRequest = CommonRequestDto(requestParameters = mockk())
        coEvery { api.getFavorites("", fakeRequest) } returns fakeResponse

        pagingSource = CommonPagingSource(
            apiCall = { api.getFavorites("", fakeRequest) },
            baseRequest = fakeRequest
        )

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertTrue(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page
        assertEquals(fakeItems, page.data)
        assertNull(page.prevKey)
        assertEquals(2, page.nextKey)
    }

    @Test
    fun `load returns Error on failure`() = runTest {
        val fakeRequest = CommonRequestDto(requestParameters = mockk())

        val body = "".toResponseBody("application/json".toMediaType())
        coEvery { api.getFavorites("", fakeRequest) } returns Response.error(600, body)

        pagingSource = CommonPagingSource(
            apiCall = { api.getFavorites("", fakeRequest) },
            baseRequest = fakeRequest
        )

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertTrue(result is PagingSource.LoadResult.Error)
        val error = result as PagingSource.LoadResult.Error
        assertEquals(R.string.unknown_message.toString(), error.throwable.message)
    }
}
