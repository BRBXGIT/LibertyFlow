package com.brbx.home.view_model.model.state

import androidx.compose.runtime.Immutable
import arrow.optics.optics
import com.brbx.design_system.component.precollection.PrecollectionModel
import com.brbx.home.common.HomeStrings
import com.brbx.ui_compose.common.BrbxIcon
import com.brbx.ui_compose.common.BrbxText
import com.brbx.ui_compose.common.toBrbxIcon
import com.brbx.ui_compose.common.toBrbxText
import dev.chiksmedina.solar.BoldSolar
import dev.chiksmedina.solar.OutlineSolar
import dev.chiksmedina.solar.bold.Arrows
import dev.chiksmedina.solar.bold.ArrowsAction
import dev.chiksmedina.solar.bold.DesignTools
import dev.chiksmedina.solar.bold.arrows.RoundArrowRight
import dev.chiksmedina.solar.bold.arrowsaction.UndoLeft
import dev.chiksmedina.solar.bold.designtools.Pallete2
import dev.chiksmedina.solar.outline.Arrows
import dev.chiksmedina.solar.outline.arrows.AltArrowRight

@Immutable
@optics
internal sealed interface Tile {

    val title: BrbxText
    val description: BrbxText
    val icon: BrbxIcon
    val precollection: PrecollectionModel?
    val isPrecollectionVisible: Boolean

    companion object

    @Immutable
    @optics
    data class LatestWatching(
        override val title: BrbxText,
        override val description: BrbxText = HomeStrings.user_watched_tile_description.toBrbxText(),
        override val icon: BrbxIcon = BoldSolar.ArrowsAction.UndoLeft.toBrbxIcon(),
        override val precollection: PrecollectionModel = PrecollectionModel(
            text = HomeStrings.user_watched_tile_precollection_title.toBrbxText(),
            icon = BoldSolar.Arrows.RoundArrowRight.toBrbxIcon(),
        ),
        override val isPrecollectionVisible: Boolean = false,
        val animeId: Int,
    ) : Tile { companion object }

    @Immutable
    @optics
    data class Theme(
        override val title: BrbxText = HomeStrings.theme_tile_title.toBrbxText(),
        override val description: BrbxText = HomeStrings.theme_tile_description.toBrbxText(),
        override val icon: BrbxIcon = BoldSolar.DesignTools.Pallete2.toBrbxIcon(),
        override val precollection: PrecollectionModel = PrecollectionModel(
            text = HomeStrings.theme_tile_precollection_text.toBrbxText(),
            icon = OutlineSolar.Arrows.AltArrowRight.toBrbxIcon(),
        ),
        override val isPrecollectionVisible: Boolean = false,
    ) : Tile { companion object }
}