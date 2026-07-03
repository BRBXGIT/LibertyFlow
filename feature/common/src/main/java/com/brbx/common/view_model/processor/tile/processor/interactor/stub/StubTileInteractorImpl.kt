package com.brbx.common.view_model.processor.tile.processor.interactor.stub

import com.brbx.common.model.alias.CommonStrings
import com.brbx.common.view_model.processor.tile.model.CommonTileState
import com.brbx.common.view_model.processor.tile.model.TileType
import com.brbx.design_system.theme.LibertyFlowIcons
import com.brbx.ui_compose.common.toBrbxIcon
import com.brbx.ui_compose.common.toBrbxText
import dev.chiksmedina.solar.BoldSolar
import dev.chiksmedina.solar.bold.Arrows
import dev.chiksmedina.solar.bold.DesignTools
import dev.chiksmedina.solar.bold.arrows.AltArrowRight
import dev.chiksmedina.solar.bold.designtools.Pallete2

internal class StubTileInteractorImpl : StubTileInteractor {

    override suspend fun getTile(): CommonTileState {
        val random = (0..10).random()
        return if (random <= 7) {
            CommonTileState(
                type = TileType.Stub.Theme,
                title = CommonStrings.theme_tile_title.toBrbxText(),
                description = CommonStrings.theme_tile_description.toBrbxText(),
                icon = BoldSolar.DesignTools.Pallete2.toBrbxIcon(),
                precollection = CommonTileState.Precollection(
                    label = CommonStrings.theme_tile_precollection_text.toBrbxText(),
                    icon = BoldSolar.Arrows.AltArrowRight.toBrbxIcon(),
                ),
                isPrecollectionVisible = false,
            )
        } else {
            CommonTileState(
                type = TileType.Stub.PoweredByAniLiberty,
                title = CommonStrings.powered_by_ani_liberty_tile_title.toBrbxText(),
                description = CommonStrings.powered_by_ani_liberty_tile_description.toBrbxText(),
                icon = LibertyFlowIcons.Multicolored.AniLiberty.toBrbxIcon(),
                precollection = CommonTileState.Precollection(
                    label = CommonStrings.powered_by_ani_liberty_tile_precollection_text.toBrbxText(),
                    icon = BoldSolar.Arrows.AltArrowRight.toBrbxIcon(),
                ),
                isPrecollectionVisible = false,
            )
        }
    }
}