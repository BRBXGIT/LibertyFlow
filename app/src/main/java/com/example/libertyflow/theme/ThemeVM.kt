package com.example.libertyflow.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.dispatchers.Dispatcher
import com.example.common.dispatchers.LibertyFlowDispatcher
import com.example.common.vm_helpers.toEagerly
import com.example.common.vm_helpers.updatee
import com.example.data.domain.ThemeRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ThemeVM @Inject constructor(
    private val themeRepo: ThemeRepo,
    @param:Dispatcher(LibertyFlowDispatcher.IO) private val dispatcherIo: CoroutineDispatcher
): ViewModel() {
    private val _themeState = MutableStateFlow(ThemeState())
    val themeState = _themeState.toEagerly(ThemeState())

    private fun observeTheme(isSystemInDarkMode: Boolean) {
        viewModelScope.launch(dispatcherIo) {
            combine(
                flow = themeRepo.theme,
                flow2 = themeRepo.colorSystem(isSystemInDarkMode),
                flow3 = themeRepo.useExpressive
            ) { theme, colorScheme, useExpressive ->
                Triple(theme, colorScheme, useExpressive)
            }.collect { (theme, colorScheme, useExpressive) ->
                _themeState.updatee {
                    copy(
                        useExpressive = useExpressive,
                        theme = theme,
                        colorScheme = colorScheme
                    )
                }
            }
        }
    }

    fun sendIntent(intent: ThemeIntent) {
        when(intent) {
            is ThemeIntent.ObserveTheme -> observeTheme(intent.isSystemInDarkMode)
        }
    }
}