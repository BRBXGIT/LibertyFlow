package com.brbx.home.view_model.processor.tile.interactor.stub

import com.brbx.common.model.common.model.DefaultTile
import com.brbx.common.model.common.model.Tile
import com.brbx.design_system.theme.LibertyFlowIcons
import com.brbx.home.common.HomeStrings
import com.brbx.ui_compose.common.toBrbxIcon
import com.brbx.ui_compose.common.toBrbxText
import dev.chiksmedina.solar.BoldSolar
import dev.chiksmedina.solar.bold.DesignTools
import dev.chiksmedina.solar.bold.designtools.Pallete2

internal class StubTileInteractorImpl : StubTileInteractor {

    override suspend fun getTile(): DefaultTile? {
        val random = (1..10).random()
        return if (random < 8) {
            DefaultTile(
                title = HomeStrings.theme_tile_title.toBrbxText(),
                description = HomeStrings.theme_tile_description.toBrbxText(),
                icon = BoldSolar.DesignTools.Pallete2.toBrbxIcon(),
                precollection = Tile.Precollection(
                    label = HomeStrings.theme_tile_precollection_text.toBrbxText(),
                ),
            )
        } else {
            DefaultTile(
                title = HomeStrings.powered_by_ani_liberty_tile_title.toBrbxText(),
                description = HomeStrings.powered_by_ani_liberty_tile_description.toBrbxText(),
                icon = LibertyFlowIcons.Multicolored.AniLiberty.toBrbxIcon(),
                precollection = Tile.Precollection(
                    label = HomeStrings.powered_by_ani_liberty_tile_precollection_text.toBrbxText(),
                ),
            )
        }
    }
}