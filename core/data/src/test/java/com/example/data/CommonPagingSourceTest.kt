package com.example.data

import com.example.data.utils.network.paging.CommonPagingSource
import com.example.network.catalog.api.CatalogApi
import com.example.network.common.common_request_models.common_request.CommonRequestDto
import com.example.network.common.common_request_models.common_request_base.CommonRequestDtoBase
import io.mockk.mockk
import org.junit.Before

// TODO: test
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
}