package com.brbx.data.user.lists.collections.interactor

import com.brbx.domain.user.lists.collections.model.DomainCollectionsIds

internal interface CollectionsIdsInteractor {

    fun update(ids: DomainCollectionsIds)
}