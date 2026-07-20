package com.brbx.common.view_model.processor.auth.model

import androidx.compose.runtime.Immutable
import arrow.optics.optics
import com.brbx.common.view_model.processor.loading.model.CommonLoadingState

@Immutable
@optics
data class CommonAuthSheetState(
    val isAuthSheetVisible: Boolean = false,
    val isDataIncorrect: Boolean = false,
    val isPasswordVisible: Boolean = false,
    val login: String = "",
    val password: String = "",
    val loadingState: CommonLoadingState = CommonLoadingState(),
) { companion object }
