package com.brbx.design_system.component.tile

import androidx.compose.runtime.Immutable
import arrow.optics.optics
import com.brbx.design_system.component.precollection.PrecollectionModel
import com.brbx.ui_compose.common.BrbxIcon
import com.brbx.ui_compose.common.BrbxText

@Immutable
@optics
data class TileModel(
    val title: BrbxText,
    val description: BrbxText,
    val icon: BrbxIcon,
    val onClick: () -> Unit,
    val isPrecollectionVisible: Boolean = false,
    val precollection: PrecollectionModel? = null,
) { companion object }
