package com.brbx.data.user.lists.collections.collections.repository

import com.brbx.data.common.map.toDomain
import com.brbx.data.user.lists.collections.interactor.CollectionsIdsInteractor
import com.brbx.data.user.lists.collections.map.toDomain
import com.brbx.domain.model.result.DomainRequestResult
import com.brbx.domain.user.lists.collections.collections.repository.UserCollectionsRepository
import com.brbx.domain.user.lists.collections.model.DomainCollectionsIds
import com.brbx.domain.user.lists.model.DomainUserListItem
import com.brbx.network.account_user.lists.collections.collections.api.AccountUserCollectionsApi
import com.brbx.network.account_user.lists.collections.model.CollectionsIds
import com.brbx.network.account_user.lists.model.UserListItem
import com.brbx.network.base.model.result.RequestResult

internal class UserCollectionsRepositoryImpl(
    private val api: AccountUserCollectionsApi,
    private val interactor: CollectionsIdsInteractor,
) : UserCollectionsRepository {

    override suspend fun addToCollection(
        items: List<DomainUserListItem.Collection>
    ): DomainRequestResult<Unit> = executeCollectionAction(items) { dataItems ->
        api.addToCollection(dataItems)
    }

    override suspend fun deleteFromCollection(
        items: List<DomainUserListItem.Collection>
    ): DomainRequestResult<Unit> = executeCollectionAction(items) { dataItems ->
        api.deleteFromCollection(dataItems)
    }

    private suspend fun executeCollectionAction(
        items: List<DomainUserListItem.Collection>,
        call: suspend (List<UserListItem.Collection>) -> RequestResult<CollectionsIds>
    ): DomainRequestResult<Unit> {
        val dataItems = items.map { it.toData() }
        val result = call(dataItems).toDomain { it.toDomain() }

        return when (result) {
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