package com.brbx.data.user.lists.collections.ids.repository

import com.brbx.data.common.map.toDomain
import com.brbx.data.user.lists.collections.interactor.CollectionsIdsInteractor
import com.brbx.data.user.lists.collections.interactor.CollectionsIdsSource
import com.brbx.data.user.lists.collections.map.toDomain
import com.brbx.domain.model.result.DomainRequestResult
import com.brbx.domain.user.lists.collections.collections_ids.repository.UserCollectionsIdsRepository
import com.brbx.domain.user.lists.collections.model.DomainCollectionsIds
import com.brbx.network.account_user.lists.collections.collection_ids.api.AccountUserCollectionsIdsApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class UserCollectionsIdsRepositoryImpl(
    private val api: AccountUserCollectionsIdsApi,
    private val interactor: CollectionsIdsInteractor,
    private val source: CollectionsIdsSource,
) : UserCollectionsIdsRepository {

    override fun getIds(): Flow<DomainRequestResult<DomainCollectionsIds>> =
        flow {
            if (source.ids.value == null) {
                val result = api.getIds().toDomain { it.toDomain() }
            }
        }
}