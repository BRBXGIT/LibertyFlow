package com.brbx.common.view_model

import com.brbx.common.model.alias.CommonStrings
import com.brbx.mvi_compose.effects.BrbxEffect
import com.brbx.ui_compose.common.BrbxText
import com.brbx.ui_compose.common.toBrbxText
import com.brbx.ui_compose.components.complex.snackbar.config.BrbxSnackbarDuration
import com.brbx.ui_compose.components.complex.snackbar.config.DefaultBrbxSnackbarConfig

fun LibertyFlowMviScope<*>.postNetworkExceptionSnackbar(
    exception: BrbxText,
    onButtonClick: () -> Unit,
) {
    postCommonEffect(
        BrbxEffect.ShowSnackbar(
            config = DefaultBrbxSnackbarConfig(
                text = exception,
                duration = BrbxSnackbarDuration.Infinite,
                isDismissable = false,
                buttonText = CommonStrings.retry.toBrbxText(),
                onButtonClick = onButtonClick,
            )
        )
    )
}

fun LibertyFlowViewModel<*, *>.postNetworkExceptionSnackbar(
    exception: BrbxText,
    onButtonClick: () -> Unit,
) {
    dispatchCommonEffect(
        BrbxEffect.ShowSnackbar(
            config = DefaultBrbxSnackbarConfig(
                text = exception,
                duration = BrbxSnackbarDuration.Infinite,
                isDismissable = false,
                buttonText = CommonStrings.retry.toBrbxText(),
                onButtonClick = onButtonClick,
            )
        )
    )
}