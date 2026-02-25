package com.example.common.ui_helpers.effects

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * A global observer for [UiEffect] streams.
 * * This Composable uses [LaunchedEffect] to collect effects from the ViewModel
 * and execute them exactly once.
 * @param effects The stream of effects to handle (Channel).
 * @param navController The controller used for [UiEffect.Navigate] actions.
 * @param snackbarHostState The state used to display snackbars. Can be null if
 * the screen doesn't support snackbars.
 * @param context [Context] for starting activities and getting string resources
 * passed as parameter for testing
 */
@Composable
fun HandleCommonEffects(
    effects: Flow<UiEffect>,
    navController: NavController,
    snackbarHostState: SnackbarHostState? = null,
    context: Context = LocalContext.current
) {
    LaunchedEffect(Unit) {
        effects.collect { effect ->
            when (effect) {
                // Snackbars
                is UiEffect.ShowSnackbarWithAction -> {
                    launch {
                        val result = snackbarHostState?.showSnackbar(
                            message = context.getString(effect.messageRes),
                            actionLabel = effect.actionLabel,
                        )

                        if (result == SnackbarResult.ActionPerformed) {
                            effect.action?.invoke()
                        }
                    }
                }
                is UiEffect.ShowSimpleSnackbar -> {
                    launch {
                        snackbarHostState?.showSnackbar(
                            withDismissAction = true,
                            message = context.getString(effect.messageRes)
                        )
                    }
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