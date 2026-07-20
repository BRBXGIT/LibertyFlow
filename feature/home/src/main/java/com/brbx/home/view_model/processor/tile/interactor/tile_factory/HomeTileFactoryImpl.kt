package com.brbx.home.view_model.processor.tile.interactor.tile_factory

import com.brbx.common.model.common.model.DefaultTile
import com.brbx.common.model.common.model.Tile
import com.brbx.design_system.theme.LibertyFlowIcons
import com.brbx.domain.local_dbs.watching_anime.use_case.anime.model.DomainLatestWatchingAnime
import com.brbx.home.common.HomeStrings
import com.brbx.ui_compose.common.toBrbxIcon
import com.brbx.ui_compose.common.toBrbxText
import dev.chiksmedina.solar.BoldSolar
import dev.chiksmedina.solar.bold.ArrowsAction
import dev.chiksmedina.solar.bold.DesignTools
import dev.chiksmedina.solar.bold.arrowsaction.UndoLeft
import dev.chiksmedina.solar.bold.designtools.Pallete2

internal class HomeTileFactoryImpl : HomeTileFactory {

    override fun createThemeTile(): DefaultTile =
        DefaultTile(
            title = HomeStrings.theme_tile_title.toBrbxText(),
            description = HomeStrings.theme_tile_description.toBrbxText(),
            icon = BoldSolar.DesignTools.Pallete2.toBrbxIcon(),
            precollection = Tile.Precollection(
                label = HomeStrings.theme_tile_precollection_text.toBrbxText(),
            ),
        )

    override fun createAniLibertyTile(): DefaultTile =
        DefaultTile(
            title = HomeStrings.powered_by_ani_liberty_tile_title.toBrbxText(),
            description = HomeStrings.powered_by_ani_liberty_tile_description.toBrbxText(),
            icon = LibertyFlowIcons.Multicolored.AniLiberty.toBrbxIcon(),
            precollection = Tile.Precollection(
                label = HomeStrings.powered_by_ani_liberty_tile_precollection_text.toBrbxText(),
            ),
        )

    override fun createLatestWatchingTile(
        anime: DomainLatestWatchingAnime
    ): DefaultTile =
        DefaultTile(
            title = anime.title.toBrbxText(),
            description = HomeStrings.user_watched_tile_description.toBrbxText(),
            icon = BoldSolar.ArrowsAction.UndoLeft.toBrbxIcon(),
            precollection = Tile.Precollection(
                label = HomeStrings.user_watched_tile_precollection_title.toBrbxText(),
            ),
        )
}