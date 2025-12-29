package com.example.common

import androidx.lifecycle.ViewModel
import com.example.common.vm_helpers.toEagerly
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class UiVM: ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.toEagerly(UiState())

    fun sendIntent(intent: UiIntent) {
        when(intent) {
            is UiIntent.UpdateSelectedRoute -> _uiState.update { it.setRoute(intent.route) }
        }
    }
}