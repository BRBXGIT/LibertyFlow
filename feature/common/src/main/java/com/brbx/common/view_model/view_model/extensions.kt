package com.brbx.common.view_model.view_model

import arrow.optics.Lens
import com.brbx.common.model.alias.CommonStrings
import com.brbx.common.view_model.processor.loading.model.CommonLoadingState
import com.brbx.mvi_compose.effects.BrbxEffect
import com.brbx.ui_compose.common.BrbxText
import com.brbx.ui_compose.common.toBrbxText
import com.brbx.ui_compose.components.complex.snackbar.common.BrbxSnackbarDuration
import com.brbx.ui_compose.components.complex.snackbar.loading_snackbar.config.DefaultBrbxLoadingSnackbarConfig
import com.brbx.ui_compose.components.complex.snackbar.snackbar.config.DefaultBrbxInfoSnackbarConfig
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

inline fun LibertyFlowMviScope<*>.postExceptionSnackbar(
    exception: BrbxText,
    dismissable: Boolean = false,
    buttonText: BrbxText = CommonStrings.retry.toBrbxText(),
    crossinline onButtonClick: () -> Unit,
) {
    postCommonEffect(
        BrbxEffect.ShowSnackbar(
            config = DefaultBrbxInfoSnackbarConfig(
                text = exception,
                duration = BrbxSnackbarDuration.Infinite,
                isDismissable = dismissable,
                buttonText = buttonText,
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
            config = DefaultBrbxInfoSnackbarConfig(
                text = exception,
                duration = BrbxSnackbarDuration.Infinite,
                isDismissable = dismissable,
            )
        )
    )
}

fun LibertyFlowMviScope<*>.postLoadingSnackbar(
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

fun LibertyFlowMviScope<*>.removeLoadingSnackbar(
    loadingSnackbarId: String = "loading_snackbar_id",
) {
    postCommonEffect(BrbxEffect.RemoveSnackbarById(loadingSnackbarId))
}

suspend inline fun <State, R> LibertyFlowMviScope<State>.makeNetworkCall(
    loadingLens: Lens<State, CommonLoadingState>? = null,
    callDelay: Long = 0L,
    crossinline call: suspend () -> R,
): R {
    loadingLens?.let {
        updateState { loadingLens.modify(source = this) { it.copy(isLoading = true, isException = false) } }
    }
    val result = call()
    delay(duration = callDelay.milliseconds)
    loadingLens?.let {
        updateState { loadingLens.modify(source = this) { it.copy(isLoading = false) } }
    }
    return result
}