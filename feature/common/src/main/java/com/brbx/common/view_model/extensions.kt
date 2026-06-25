package com.brbx.common.view_model

import com.brbx.mvi.view_model.BrbxMviScope
import com.brbx.mvi_compose.effects.BrbxEffect
import com.brbx.ui_compose.components.complex.snackbar.config.BrbxSnackbarConfig

fun <State> BrbxMviScope<State, BrbxEffect, Unit>.postSnackbarEffect(
    config: BrbxSnackbarConfig,
) {
    postCommonEffect(BrbxEffect.ShowSnackbar(config))
}