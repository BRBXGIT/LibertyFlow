package com.brbx.data.user.lists.collections.interactor

import com.brbx.domain.user.lists.collections.model.DomainCollectionsIds
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal class CollectionsIdsInteractorImpl : CollectionsIdsInteractor, CollectionsIdsSource {

    private val _ids = MutableStateFlow<DomainCollectionsIds?>(value = null)
    override val ids = _ids.asStateFlow()

    override fun update(ids: DomainCollectionsIds) {
        _ids.update { ids }
    }
}