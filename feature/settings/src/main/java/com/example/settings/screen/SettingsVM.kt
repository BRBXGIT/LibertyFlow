package com.example.settings.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.dispatchers.Dispatcher
import com.example.common.dispatchers.LibertyFlowDispatcher
import com.example.common.ui_helpers.effects.UiEffect
import com.example.common.vm_helpers.toWhileSubscribed
import com.example.data.domain.PlayerSettingsRepo
import com.example.data.domain.ThemeRepo
import com.example.data.models.player.VideoQuality
import com.example.data.models.theme.ColorSchemeValue
import com.example.data.models.theme.ThemeValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsVM @Inject constructor(
    private val themeRepo: ThemeRepo,
    private val playerSettingsRepo: PlayerSettingsRepo,
    @param:Dispatcher(LibertyFlowDispatcher.IO) private val dispatcherIo: CoroutineDispatcher
): ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.toWhileSubscribed(SettingsState())

    private val _effects = Channel<UiEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    init { observePrefs() }

    // --- Intents ---
    fun sendIntent(intent: SettingsIntent) {
        when(intent) {
            // Theme
            is SettingsIntent.SetColorScheme -> setColorScheme(intent.colorSchemeValue)
            is SettingsIntent.SetTheme -> setTheme(intent.theme)
            SettingsIntent.ToggleUseExpressive -> toggleUseExpressive()

            // Player
            is SettingsIntent.SetQuality -> setVideoQuality(intent.quality)
            SettingsIntent.ToggleAutoPlay -> toggleAutoPlay()
            SettingsIntent.ToggleAutoSkipOpening -> toggleAutoSkipOpening()
            SettingsIntent.ToggleShowSkipOpeningButton -> toggleShowSkipOpeningButton()

            // Screen ui
            SettingsIntent.ToggleIsQualityBSVisible -> _state.update { it.toggleQualityBS() }
        }
    }

    // --- Effects ---
    fun sendEffect(effect: UiEffect) = viewModelScope.launch(dispatcherIo) {
        _effects.send(effect)
    }

    // --- Theme ---
    private fun toggleUseExpressive() = viewModelScope.launch(dispatcherIo) {
        themeRepo.saveUseExpressive(!_state.value.themeSettings.useExpressive)
    }

    private fun setTheme(theme: ThemeValue) = viewModelScope.launch(dispatcherIo) {
        themeRepo.saveTheme(theme)
    }

    private fun setColorScheme(colorScheme: ColorSchemeValue) = viewModelScope.launch(dispatcherIo) {
        themeRepo.saveColorSystem(colorScheme)
    }

    // --- Player settings ---
    private fun setVideoQuality(videoQuality: VideoQuality) = viewModelScope.launch(dispatcherIo) {
        playerSettingsRepo.saveQuality(videoQuality)
    }

    private fun toggleShowSkipOpeningButton() = viewModelScope.launch(dispatcherIo) {
        playerSettingsRepo.saveShowSkipOpeningButton(!_state.value.playerSettings.showSkipOpeningButton)
    }

    private fun toggleAutoSkipOpening() = viewModelScope.launch(dispatcherIo) {
        playerSettingsRepo.saveAutoSkipOpening(!_state.value.playerSettings.autoSkipOpening)
    }

    private fun toggleAutoPlay() = viewModelScope.launch(dispatcherIo) {
        playerSettingsRepo.saveAutoPlay(!_state.value.playerSettings.autoPlay)
    }

    // --- Observe prefs ---
    private fun observePrefs() {
        viewModelScope.launch(dispatcherIo) {
            combine(
                themeRepo.libertyFlowTheme,
                playerSettingsRepo.playerSettings
            ) { theme, playerSettings ->
                theme to playerSettings
            }.collect { (theme, playerSettings) ->
                _state.update {
                    it.copy(
                        themeSettings = theme,
                        playerSettings = playerSettings
                    )
                }
            }
        }
    }
}