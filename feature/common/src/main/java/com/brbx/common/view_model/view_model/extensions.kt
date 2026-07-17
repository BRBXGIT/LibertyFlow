package com.brbx.common.view_model.view_model

import arrow.optics.Lens
import com.brbx.common.model.alias.CommonStrings
import com.brbx.common.view_model.processor.loading.model.CommonLoadingState
import com.brbx.mvi_compose.effects.BrbxEffect
import com.brbx.ui_compose.common.BrbxText
import com.brbx.ui_compose.common.toBrbxText
import com.brbx.ui_compose.components.complex.snackbar.config.BrbxSnackbarDuration
import com.brbx.ui_compose.components.complex.snackbar.config.DefaultBrbxSnackbarConfig
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

inline fun LibertyFlowMviScope<*>.postExceptionSnackbar(
    exception: BrbxText,
    dismissable: Boolean = false,
    crossinline onButtonClick: () -> Unit,
) {
    postCommonEffect(
        BrbxEffect.ShowSnackbar(
            config = DefaultBrbxSnackbarConfig(
                text = exception,
                duration = BrbxSnackbarDuration.Infinite,
                isDismissable = dismissable,
                buttonText = CommonStrings.retry.toBrbxText(),
                onButtonClick = { onButtonClick() },
            )
        )
    )
}

fun LibertyFlowMviScope<*>.postExceptionSnackbar(
    exception: BrbxText,
    dismissable: Boolean = true,
) {
    postCommonEffect(
        BrbxEffect.ShowSnackbar(
            config = DefaultBrbxSnackbarConfig(
                text = exception,
                duration = BrbxSnackbarDuration.Infinite,
                isDismissable = dismissable,
            )
        )
    )
}

suspend inline fun <State, R> LibertyFlowMviScope<State>.makeNetworkCall(
    loadingLens: Lens<State, CommonLoadingState>,
    callDelay: Long = 0L,
    crossinline call: suspend () -> R,
): R {
    updateState { loadingLens.modify(source = this) { it.copy(isLoading = true, isException = false) } }
    val result = call()
    delay(duration = callDelay.milliseconds)
    updateState { loadingLens.modify(source = this) { it.copy(isLoading = false) } }
    return result
}