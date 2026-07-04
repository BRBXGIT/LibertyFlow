package com.brbx.common.view_model.processor.selection.model

import androidx.compose.runtime.Immutable
import arrow.optics.optics
import com.brbx.common.view_model.processor.loading.model.CommonLoadingState

@Immutable
@optics
data class CommonSelectionState(
    val ids: Set<Int> = emptySet(),
    val isCollectionsSheetVisible: Boolean = false,
    val loadingState: CommonLoadingState = CommonLoadingState(),
) {
    val isInSelectionMode = ids.isNotEmpty()

    companion object
}
