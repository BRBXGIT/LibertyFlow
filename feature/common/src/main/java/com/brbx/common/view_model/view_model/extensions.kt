package com.brbx.common.view_model.view_model

import arrow.optics.Lens
import com.brbx.common.model.alias.CommonStrings
import com.brbx.common.view_model.model.state.CommonLoadingState
import com.brbx.mvi_compose.effects.BrbxEffect
import com.brbx.ui_compose.common.BrbxText
import com.brbx.ui_compose.common.toBrbxText
import com.brbx.ui_compose.components.complex.snackbar.config.BrbxSnackbarDuration
import com.brbx.ui_compose.components.complex.snackbar.config.DefaultBrbxSnackbarConfig

inline fun LibertyFlowMviScope<*>.postNetworkExceptionSnackbar(
    exception: BrbxText,
    crossinline onButtonClick: () -> Unit,
) {
    postCommonEffect(
        BrbxEffect.ShowSnackbar(
            config = DefaultBrbxSnackbarConfig(
                text = exception,
                duration = BrbxSnackbarDuration.Infinite,
                isDismissable = false,
                buttonText = CommonStrings.retry.toBrbxText(),
                onButtonClick = { onButtonClick() },
            )
        )
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