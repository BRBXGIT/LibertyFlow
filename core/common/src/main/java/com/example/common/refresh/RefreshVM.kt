package com.example.common.refresh

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.dispatchers.Dispatcher
import com.example.common.dispatchers.LibertyFlowDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RefreshVM @Inject constructor(
    @param:Dispatcher(LibertyFlowDispatcher.IO) private val dispatcherIo: CoroutineDispatcher
): ViewModel() {

    private val _refreshEffects = Channel<RefreshEffect>(Channel.BUFFERED)
    val refreshEffects = _refreshEffects.receiveAsFlow()

    fun sendEffect(effect: RefreshEffect) {
        viewModelScope.launch(dispatcherIo) {
            _refreshEffects.send(effect)
        }
    }
}