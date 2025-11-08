package com.example.common

import androidx.lifecycle.ViewModel
import com.example.common.vm_helpers.toEagerly
import com.example.common.vm_helpers.update
import kotlinx.coroutines.flow.MutableStateFlow

class UiVM: ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.toEagerly(UiState())

    fun sendIntent(intent: UiIntent) {
        when(intent) {
            is UiIntent.ChangeSelectedRoute -> _uiState.update { copy(selectedRoute = intent.route) }
        }
    }
}