package com.brbx.data.user.lists.collections.ids.repository

import com.brbx.data.user.lists.collections.interactor.CollectionsIdsInteractor
import com.brbx.domain.model.result.DomainRequestResult
import com.brbx.domain.user.lists.collections.collections_ids.repository.UserCollectionsIdsRepository
import com.brbx.domain.user.lists.collections.model.DomainCollectionIds
import com.brbx.network.account_user.lists.collections.collection_ids.api.AccountUserCollectionsIdsApi
import kotlinx.coroutines.flow.Flow

internal class UserCollectionsIdsRepositoryImpl(
    private val api: AccountUserCollectionsIdsApi,
    private val interactor: CollectionsIdsInteractor,
) : UserCollectionsIdsRepository {

    override fun getIds(): Flow<DomainRequestResult<List<DomainCollectionIds>>> {

    }
}