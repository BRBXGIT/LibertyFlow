package com.brbx.data.network.user.lists.collections.collections.repository

import com.brbx.data.network.api.executor.ApiCallExecutor
import com.brbx.data.network.user.lists.collections.interactor.CollectionsIdsInteractor
import com.brbx.data.network.user.lists.collections.map.toDomain
import com.brbx.domain.network.model.result.DomainRequestResult
import com.brbx.domain.network.user.lists.collections.collections.repository.UserCollectionsRepository
import com.brbx.domain.network.user.lists.collections.model.DomainCollectionsIds
import com.brbx.domain.network.user.lists.model.DomainUserListItem
import com.brbx.network.account_user.lists.collections.collections.api.AccountUserCollectionsApi
import com.brbx.network.account_user.lists.model.UserListItem

internal class UserCollectionsRepositoryImpl(
    private val api: AccountUserCollectionsApi,
    private val interactor: CollectionsIdsInteractor,
    private val executor: ApiCallExecutor,
) : UserCollectionsRepository {

    override suspend fun addToCollection(
        items: List<DomainUserListItem.Collection>
    ): DomainRequestResult<Unit> = executeCollectionAction(items) { dataItems ->
        executor.execute(mapper = { it.toDomain() }) { api.addToCollection(dataItems) }
    }

    override suspend fun deleteFromCollection(
        items: List<DomainUserListItem.Collection>
    ): DomainRequestResult<Unit> = executeCollectionAction(items) { dataItems ->
        executor.execute(mapper = { it.toDomain() }) { api.deleteFromCollection(dataItems) }
    }

    private suspend fun executeCollectionAction(
        items: List<DomainUserListItem.Collection>,
        call: suspend (List<UserListItem.Collection>) -> DomainRequestResult<DomainCollectionsIds>
    ): DomainRequestResult<Unit> {
        val dataItems = items.map { it.toData() }
        return when (val result = call(dataItems)) {
            is DomainRequestResult.Error -> result
            is DomainRequestResult.Success<DomainCollectionsIds> -> {
                interactor.update(ids = result.data)
                DomainRequestResult.Success(data = Unit)
            }
        }
    }

    private fun DomainUserListItem.Collection.toData(): UserListItem.Collection =
        UserListItem.Collection(
            id = this.id,
            collection = this.collection.dataValue,
        )
}