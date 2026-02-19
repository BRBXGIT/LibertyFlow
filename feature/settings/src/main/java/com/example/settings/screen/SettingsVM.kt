package com.example.settings.screen

import androidx.lifecycle.viewModelScope
import com.example.common.dispatchers.Dispatcher
import com.example.common.dispatchers.LibertyFlowDispatcher
import com.example.common.ui_helpers.effects.UiEffect
import com.example.common.vm_helpers.player.BasePlayerSettingsVM
import com.example.common.vm_helpers.utils.toWhileSubscribed
import com.example.data.domain.PlayerSettingsRepo
import com.example.data.domain.ThemeRepo
import com.example.data.models.theme.ColorSchemeValue
import com.example.data.models.theme.TabType
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
): BasePlayerSettingsVM(playerSettingsRepo, dispatcherIo) {

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
            SettingsIntent.ToggleTabType -> toggleTabType()

            // Player
            is SettingsIntent.SetQuality -> saveVideoQuality(intent.quality)
            SettingsIntent.ToggleAutoPlay -> toggleAutoPlay(!_state.value.playerSettings.autoPlay)
            SettingsIntent.ToggleAutoSkipOpening -> toggleAutoSkipOpening(!_state.value.playerSettings.autoSkipOpening)
            SettingsIntent.ToggleShowSkipOpeningButton -> toggleShowSkipOpeningButton(!_state.value.playerSettings.showSkipOpeningButton)
            SettingsIntent.ToggleIsCropped -> toggleIsCropped(!_state.value.playerSettings.isCropped)

            // Screen ui
            SettingsIntent.ToggleIsQualityBSVisible -> _state.update { it.toggleQualityBS() }
        }
    }

    // --- Effects ---
    fun sendEffect(effect: UiEffect) =
        viewModelScope.launch { _effects.send(effect) }

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

    private fun toggleTabType() = viewModelScope.launch(dispatcherIo) {
        val type = _state.value.themeSettings.tabType
        when(type) {
            TabType.M3 -> themeRepo.saveTab(TabType.Tablet)
            TabType.Tablet -> themeRepo.saveTab(TabType.Chips)
            TabType.Chips -> themeRepo.saveTab(TabType.M3)
        }
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