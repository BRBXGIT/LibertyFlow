package com.brbx.data.user.lists.collections.interactor

import com.brbx.domain.user.lists.collections.model.DomainCollectionIds
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal class CollectionsIdsInteractorImpl : CollectionsIdsInteractor, CollectionsIdsSource {

    private val _ids = MutableStateFlow<List<DomainCollectionIds>?>(value = null)
    override val ids = _ids.asStateFlow()

    override fun update(ids: List<DomainCollectionIds>) {
        _ids.update { ids }
    }
}