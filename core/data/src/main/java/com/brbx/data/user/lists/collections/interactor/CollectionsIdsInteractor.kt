package com.brbx.data.user.lists.collections.interactor

import com.brbx.domain.user.lists.collections.model.DomainCollectionIds

internal interface CollectionsIdsInteractor {

    fun update(ids: List<DomainCollectionIds>)
}