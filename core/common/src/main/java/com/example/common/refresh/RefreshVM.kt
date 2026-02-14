package com.example.common.refresh

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * A centralized ViewModel responsible for broadcasting [RefreshEffect] signals
 * across the application using a buffered [Channel].
 * * This allows disparate parts of the UI to react to data changes (e.g., a 'new item added to favorites'
 * in one screen updating a list in another).
 */
class RefreshVM(): ViewModel() {

    private val _refreshEffects = Channel<RefreshEffect>(Channel.BUFFERED)
    val refreshEffects = _refreshEffects.receiveAsFlow()

    fun sendEffect(effect: RefreshEffect) =
        viewModelScope.launch { _refreshEffects.send(effect) }
}