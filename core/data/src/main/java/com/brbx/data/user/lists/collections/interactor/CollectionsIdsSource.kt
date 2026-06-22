package com.brbx.data.user.lists.collections.interactor

import com.brbx.domain.user.lists.collections.model.DomainCollectionsIds
import kotlinx.coroutines.flow.StateFlow

internal interface CollectionsIdsSource {

    val ids: StateFlow<DomainCollectionsIds?>
}