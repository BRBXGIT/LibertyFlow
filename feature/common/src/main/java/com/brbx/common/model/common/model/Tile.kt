package com.brbx.common.model.common.model

import androidx.compose.runtime.Immutable
import arrow.optics.optics
import com.brbx.ui_compose.common.BrbxIcon
import com.brbx.ui_compose.common.BrbxText
import com.brbx.ui_compose.common.toBrbxIcon
import dev.chiksmedina.solar.BoldSolar
import dev.chiksmedina.solar.OutlineSolar
import dev.chiksmedina.solar.bold.Arrows
import dev.chiksmedina.solar.bold.arrows.AltArrowRight
import dev.chiksmedina.solar.outline.Arrows
import dev.chiksmedina.solar.outline.arrows.AltArrowRight

@Immutable
interface Tile {

    val title: BrbxText
    val description: BrbxText
    val icon: BrbxIcon
    val precollection: Precollection?
    val isPrecollectionVisible: Boolean

    @Immutable
    @optics
    data class Precollection(
        val label: BrbxText,
        val icon: BrbxIcon? = OutlineSolar.Arrows.AltArrowRight.toBrbxIcon(),
    ) { companion object }
}