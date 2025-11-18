package com.example.design_system.components.snackbars

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope

@Composable
fun getSnackbarState(): SnackbarState {
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarScope = rememberCoroutineScope()
    SnackbarObserver(snackbarHostState, snackbarScope)

    return SnackbarState(snackbarScope = snackbarScope, snackbarHostState = snackbarHostState)
}