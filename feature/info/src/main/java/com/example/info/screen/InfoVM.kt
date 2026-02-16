package com.example.info.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.ui_helpers.effects.UiEffect
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for managing the state and side effects of the Information screen.
 *
 * This implementation uses [Channel] to handle one-time UI events (Side Effects),
 * ensuring that events like navigation or clipboard actions are processed exactly once.
 */
class InfoVM: ViewModel() {

    private val _commonEffects = Channel<UiEffect>(Channel.BUFFERED)
    val commonEffects = _commonEffects.receiveAsFlow()

    private val _effects = Channel<InfoEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    // --- Effects ---
    /**
     * Dispatches an effect to the common effect stream.
     * @param effect The [UiEffect] to be processed by the UI layer.
     */
    fun sendCommonEffect(effect: UiEffect) =
        viewModelScope.launch { _commonEffects.send(effect) }

    /**
     * Dispatches an effect specific to the Information screen.
     * @param effect The [InfoEffect] to be processed by the UI layer.
     */
    fun sendEffect(effect: InfoEffect) =
        viewModelScope.launch { _effects.send(effect) }
}