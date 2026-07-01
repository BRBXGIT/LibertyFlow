package com.brbx.design_system.component.precollection

import androidx.compose.runtime.Immutable
import arrow.optics.optics
import com.brbx.ui_compose.common.BrbxIcon
import com.brbx.ui_compose.common.BrbxText
import com.brbx.ui_compose.components.complex.precollection.precollection.BrbxPrecollectionAppearance

@Immutable
@optics
data class PrecollectionModel(
    val text: BrbxText,
    val icon: BrbxIcon,
    val onClick: () -> Unit,
    val appearance: BrbxPrecollectionAppearance = PrecollectionConstants.precollectionAppearance,
) { companion object }