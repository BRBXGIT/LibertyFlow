package com.brbx.data.network.user.lists.collections.ids.repository

import com.brbx.data.network.api.executor.ApiCallExecutor
import com.brbx.data.network.user.lists.collections.interactor.CollectionsIdsInteractor
import com.brbx.data.network.user.lists.collections.interactor.CollectionsIdsSource
import com.brbx.data.network.user.lists.collections.map.toDomain
import com.brbx.domain.network.model.result.DomainRequestResult
import com.brbx.domain.network.user.lists.collections.collections_ids.repository.UserCollectionsIdsRepository
import com.brbx.domain.network.user.lists.collections.model.DomainCollectionsIds
import com.brbx.network.account_user.lists.collections.collection_ids.api.AccountUserCollectionsIdsApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class UserCollectionsIdsRepositoryImpl(
    private val api: AccountUserCollectionsIdsApi,
    private val interactor: CollectionsIdsInteractor,
    private val source: CollectionsIdsSource,
    private val executor: ApiCallExecutor,
) : UserCollectionsIdsRepository {

    override fun getIds(): Flow<DomainRequestResult<DomainCollectionsIds>> =
        flow {
            if (source.ids.value == null) {
                val result = executor.execute(mapper = { it.toDomain() }) { api.getIds() }
                when (result) {
                    is DomainRequestResult.Success -> interactor.update(ids = result.data)
                    is DomainRequestResult.Error -> {
                        emit(value = result)
                        return@flow
                    }
                }
            }
            source.ids.collect { cachedIds ->
                if (cachedIds != null) {
                    emit(value = DomainRequestResult.Success(data = cachedIds))
                }
            }
        }
}