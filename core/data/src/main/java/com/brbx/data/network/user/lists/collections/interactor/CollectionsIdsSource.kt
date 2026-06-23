package com.brbx.data.network.user.lists.collections.interactor

import com.brbx.domain.network.user.lists.collections.model.DomainCollectionsIds
import kotlinx.coroutines.flow.StateFlow

internal interface CollectionsIdsSource {

    val ids: StateFlow<DomainCollectionsIds?>
}