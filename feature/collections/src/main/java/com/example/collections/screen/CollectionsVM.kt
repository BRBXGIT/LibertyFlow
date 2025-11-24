@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.collections.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.common.vm_helpers.toLazily
import com.example.data.domain.CollectionsRepo
import com.example.data.models.common.request.common_request.UiCommonRequestWithCollectionType
import com.example.data.models.common.request.request_parameters.UiShortRequestParameters
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class CollectionsVM @Inject constructor(
    private val collectionsRepo: CollectionsRepo,
): ViewModel() {

    private val _collectionsState = MutableStateFlow(CollectionsState())
    val collectionsState = _collectionsState.toLazily(CollectionsState())

    private val collectionParameters = _collectionsState
        .map { state -> CollectionParameters(query = state.query, selectedCollection = state.selectedCollection) }
        .distinctUntilChanged()

    val collectionAnime = collectionParameters.flatMapLatest { parameters ->
        val request = UiCommonRequestWithCollectionType(
            requestParameters = UiShortRequestParameters(search = parameters.query),
            collectionType = parameters.selectedCollection
        )
        collectionsRepo.getAnimeInCollection(request)
    }.cachedIn(viewModelScope)
}