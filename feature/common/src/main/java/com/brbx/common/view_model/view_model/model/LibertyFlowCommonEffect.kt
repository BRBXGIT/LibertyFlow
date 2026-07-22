package com.brbx.common.view_model.view_model.model

import androidx.compose.runtime.Immutable
import com.brbx.mvi_compose.effects.BrbxCommonEffect

@Immutable
sealed interface LibertyFlowCommonEffect : BrbxCommonEffect {

    data object RequireAuth : LibertyFlowCommonEffect
}