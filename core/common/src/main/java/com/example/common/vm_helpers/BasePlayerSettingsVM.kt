package com.example.common.vm_helpers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.dispatchers.Dispatcher
import com.example.common.dispatchers.LibertyFlowDispatcher
import com.example.data.domain.PlayerSettingsRepo
import com.example.data.models.player.VideoQuality
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

/**
 * Base class for vms using player settings
 * @param playerSettingsRepo is the repo with player settings
 * @param dispatcherIo is the dispatcher for IO operations
 */
abstract class BasePlayerSettingsVM(
    private val playerSettingsRepo: PlayerSettingsRepo,
    @param:Dispatcher(LibertyFlowDispatcher.IO) private val dispatcherIo: CoroutineDispatcher
): ViewModel() {
    protected fun saveVideoQuality(videoQuality: VideoQuality) = viewModelScope.launch(dispatcherIo) {
        playerSettingsRepo.saveQuality(videoQuality)
    }

    protected fun toggleShowSkipOpeningButton(value: Boolean) = viewModelScope.launch(dispatcherIo) {
        playerSettingsRepo.saveShowSkipOpeningButton(value)
    }

    protected fun toggleAutoSkipOpening(value: Boolean) = viewModelScope.launch(dispatcherIo) {
        playerSettingsRepo.saveAutoSkipOpening(value)
    }

    protected fun toggleAutoPlay(value: Boolean) = viewModelScope.launch(dispatcherIo) {
        playerSettingsRepo.saveAutoPlay(value)
    }

    protected fun toggleIsCropped(value: Boolean) = viewModelScope.launch(dispatcherIo) {
        playerSettingsRepo.saveIsCopped(value)
    }
}