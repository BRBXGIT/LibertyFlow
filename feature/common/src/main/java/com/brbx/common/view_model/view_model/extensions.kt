package com.brbx.common.view_model.view_model

import arrow.optics.Lens
import com.brbx.common.model.alias.CommonStrings
import com.brbx.common.view_model.processor.loading.model.CommonLoadingState
import com.brbx.mvi_compose.effects.BrbxEffect
import com.brbx.ui_compose.common.BrbxText
import com.brbx.ui_compose.common.toBrbxText
import com.brbx.ui_compose.components.complex.snackbar.config.BrbxSnackbarDuration
import com.brbx.ui_compose.components.complex.snackbar.config.DefaultBrbxSnackbarConfig

fun LibertyFlowMviScope<*>.postNetworkExceptionSnackbar(
    exception: BrbxText,
    onButtonClick: (() -> Unit)? = null,
) {
    postCommonEffect(
        if (onButtonClick != null) {
            BrbxEffect.ShowSnackbar(
                config = DefaultBrbxSnackbarConfig(
                    text = exception,
                    duration = BrbxSnackbarDuration.Infinite,
                    isDismissable = false,
                    buttonText = CommonStrings.retry.toBrbxText(),
                    onButtonClick = { onButtonClick() },
                )
            )
        } else {
            BrbxEffect.ShowSnackbar(
                config = DefaultBrbxSnackbarConfig(
                    text = exception,
                    duration = BrbxSnackbarDuration.Long,
                    isDismissable = true,
                )
            )
        }
    )
}

suspend inline fun <State, R> LibertyFlowMviScope<State>.makeNetworkCall(
    loadingLens: Lens<State, CommonLoadingState>,
    crossinline call: suspend () -> R,
): R {
    updateState { loadingLens.modify(source = this) { it.copy(isLoading = true, isException = false) } }
    val result = call()
    updateState { loadingLens.modify(source = this) { it.copy(isLoading = false) } }
    return result
}