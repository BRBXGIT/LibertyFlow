package com.brbx.common.view_model.processor.selection.model

import androidx.compose.runtime.Immutable
import arrow.optics.optics

@Immutable
@optics
data class CommonSelectionState(
    val ids: Set<Int> = emptySet(),
    val isCollectionsSheetVisible: Boolean = false,
) {
    val isInSelectionMode = ids.isNotEmpty()

    companion object
}
