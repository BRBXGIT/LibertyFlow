package com.brbx.design_system.component.precollection

import androidx.compose.runtime.Immutable
import arrow.optics.optics
import com.brbx.ui_compose.common.BrbxIcon
import com.brbx.ui_compose.common.BrbxText

@Immutable
@optics
data class PrecollectionModel(
    val text: BrbxText,
    val icon: BrbxIcon,
) { companion object }