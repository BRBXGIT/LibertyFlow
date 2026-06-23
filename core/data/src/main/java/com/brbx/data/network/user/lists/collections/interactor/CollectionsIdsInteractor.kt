package com.brbx.data.network.user.lists.collections.interactor

import com.brbx.domain.network.user.lists.collections.model.DomainCollectionsIds

internal interface CollectionsIdsInteractor {

    fun update(ids: DomainCollectionsIds)
}