package com.example.data.data

import androidx.paging.PagingData
import com.example.data.domain.CatalogRepo
import com.example.network.catalog.api.CatalogApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CatalogRepoImpl @Inject constructor(
    private val api: CatalogApi
): CatalogRepo {

    override fun getAnimeByQuery(): Flow<PagingData<Any>> {
        //TODO
    }
}