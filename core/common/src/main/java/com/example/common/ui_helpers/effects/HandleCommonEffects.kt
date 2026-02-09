package com.example.common.ui_helpers.effects

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import kotlinx.coroutines.flow.Flow

// Common function to handle common effects
@Composable
fun HandleCommonEffects(
    effects: Flow<UiEffect>,
    navController: NavController,
    snackbarHostState: SnackbarHostState? = null,
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        effects.collect { effect ->
            when (effect) {
                // Snackbars
                is UiEffect.ShowSnackbarWithAction -> {
                    val result = snackbarHostState!!.showSnackbar(
                        message = context.getString(effect.messageRes),
                        actionLabel = effect.actionLabel,
                        withDismissAction = true
                    )

                    if (result == SnackbarResult.ActionPerformed) {
                        effect.action?.invoke()
                    }
                }
                is UiEffect.ShowSimpleSnackbar -> {
                    snackbarHostState!!.showSnackbar(
                        withDismissAction = true,
                        message = context.getString(effect.messageRes)
                    )
                }

                // Navigation
                is UiEffect.Navigate -> navController.navigate(effect.route)
                is UiEffect.NavigateBack -> navController.navigateUp()

                // Intents
                is UiEffect.IntentTo -> context.startActivity(effect.intent)
            }
        }
    }
}