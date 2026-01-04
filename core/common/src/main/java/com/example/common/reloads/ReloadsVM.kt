package com.example.common.reloads

import androidx.lifecycle.ViewModel
import com.example.common.vm_helpers.toEagerly
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class ReloadsVM: ViewModel() {

    private val _reloadsState = MutableStateFlow(ReloadsState())
    val reloadsState = _reloadsState.toEagerly(ReloadsState())

    fun sendIntent(intent: ReloadsIntent) {
        when(intent) {
            ReloadsIntent.ToggleFavoritesNeedReload ->
                _reloadsState.update { it.toggleFavoritesNeedReload() }
            is ReloadsIntent.UpdateCollectionNeedReload ->
                _reloadsState.update { it.updateCollectionNeedReload(intent.collection, intent.needReload) }
        }
    }
}