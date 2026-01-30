package com.example.info.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.dispatchers.Dispatcher
import com.example.common.dispatchers.LibertyFlowDispatcher
import com.example.common.ui_helpers.effects.UiEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InfoVM @Inject constructor(
    @param:Dispatcher(LibertyFlowDispatcher.IO) private val dispatcherIo: CoroutineDispatcher
): ViewModel() {

    private val _commonEffects = Channel<UiEffect>(Channel.BUFFERED)
    val commonEffects = _commonEffects.receiveAsFlow()

    private val _effects = Channel<InfoEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    // --- Effects ---
    fun sendCommonEffect(effect: UiEffect) {
        viewModelScope.launch(dispatcherIo) {
            _commonEffects.send(effect)
        }
    }

    fun sendEffect(effect: InfoEffect) {
        viewModelScope.launch(dispatcherIo) {
            _effects.send(effect)
        }
    }
}