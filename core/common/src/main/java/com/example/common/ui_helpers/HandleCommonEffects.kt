package com.example.common.ui_helpers

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import kotlinx.coroutines.flow.Flow

@Composable
fun HandleCommonEffects(
    effects: Flow<UiEffect>,
    navController: NavController,
    snackbarHostState: SnackbarHostState
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        effects.collect { effect ->
            when (effect) {
                is UiEffect.ShowSnackbar -> {
                    val result = snackbarHostState.showSnackbar(
                        message = context.getString(effect.messageRes),
                        actionLabel = effect.actionLabel
                    )

                    if (result == SnackbarResult.ActionPerformed) {
                        effect.action?.invoke()
                    }
                }

                is UiEffect.Navigate -> {
                    navController.navigate(effect.route)
                }
            }
        }
    }
}
