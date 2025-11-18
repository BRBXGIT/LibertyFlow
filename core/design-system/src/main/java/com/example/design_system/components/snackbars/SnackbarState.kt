package com.example.design_system.components.snackbars

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Immutable
import kotlinx.coroutines.CoroutineScope

@Immutable
data class SnackbarState(
    val snackbarScope: CoroutineScope,
    val snackbarHostState: SnackbarHostState
)