package com.brbx.common.view_model.view_model

import arrow.optics.Lens
import com.brbx.common.model.alias.CommonStrings
import com.brbx.common.view_model.processor.loading.model.CommonLoadingState
import com.brbx.mvi.processor.BrbxIntentProcessor
import com.brbx.mvi_compose.effects.BrbxEffect
import com.brbx.ui_compose.common.BrbxText
import com.brbx.ui_compose.common.toBrbxText
import com.brbx.ui_compose.components.complex.snackbar.common.BrbxSnackbarDuration
import com.brbx.ui_compose.components.complex.snackbar.loading_snackbar.config.DefaultBrbxLoadingSnackbarConfig
import com.brbx.ui_compose.components.complex.snackbar.snackbar.config.DefaultBrbxInfoSnackbarConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlin.time.Duration.Companion.milliseconds

abstract class LibertyFlowIntentProcessor<State, Intent : Any> :
    BrbxIntentProcessor<LibertyFlowMviScope<State>, State, Intent, BrbxEffect, Unit>() {

    // Scope's properties
    protected val state: StateFlow<State> get() = scope.state
    protected val coroutineScope: CoroutineScope get() = scope.coroutineScope

    // Scope's methods
    protected fun updateState(transform: State.() -> State) { scope.updateState(transform) }
    protected fun postCommonEffect(effect: BrbxEffect) { scope.postCommonEffect(effect) }
    protected fun postLocalEffect(effect: Unit) { scope.postLocalEffect(effect) }

    // TODO Move helpers to delegates
    // State helpers
    protected fun <Child> updateLensState(
        lens: Lens<State, Child>,
        map: (Child) -> Child,
    ) { updateState { lens.modify(source = this, map = map) } }

    // Snackbars helpers
    protected fun postExceptionSnackbar(
        exception: BrbxText,
        dismissable: Boolean = false,
        buttonText: BrbxText? = CommonStrings.retry.toBrbxText(),
        onButtonClick: (() -> Unit)? = null,
    ) {
        postCommonEffect(
            BrbxEffect.ShowSnackbar(
                config = DefaultBrbxInfoSnackbarConfig(
                    text = exception,
                    duration = BrbxSnackbarDuration.Infinite,
                    isDismissable = dismissable,
                    buttonText = buttonText,
                    onButtonClick = onButtonClick,
                )
            )
        )
    }

    protected fun postLoadingSnackbar(
        text: BrbxText,
        loadingSnackbarId: String = "loading_snackbar_id",
    ) {
        postCommonEffect(
            BrbxEffect.ShowSnackbar(
                config = DefaultBrbxLoadingSnackbarConfig(
                    id = loadingSnackbarId,
                    text = text,
                    duration = BrbxSnackbarDuration.Infinite,
                    isDismissable = false,
                )
            )
        )
    }

    protected fun removeLoadingSnackbar(
        loadingSnackbarId: String = "loading_snackbar_id",
    ) { postCommonEffect(BrbxEffect.RemoveSnackbarById(loadingSnackbarId)) }

    // Network helpers
    protected suspend fun <R> makeNetworkCall(
        loadingLens: Lens<State, CommonLoadingState>? = null,
        callDelay: Long = 0L,
        call: suspend () -> R,
    ): R {
        loadingLens?.let {
            updateLensState(loadingLens) { it.copy(isLoading = true, isException = false) }
        }
        val result = call()
        delay(duration = callDelay.milliseconds)
        loadingLens?.let {
            updateLensState(loadingLens) { it.copy(isLoading = false) }
        }
        return result
    }
}