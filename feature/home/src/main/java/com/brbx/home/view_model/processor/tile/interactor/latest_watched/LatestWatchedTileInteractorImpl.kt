package com.brbx.home.view_model.processor.tile.interactor.latest_watched

import com.brbx.common.model.common.model.DefaultTile
import com.brbx.common.model.common.model.Tile
import com.brbx.domain.local_dbs.watching_anime.use_case.anime.model.DomainLatestWatchingAnime
import com.brbx.domain.local_dbs.watching_anime.use_case.anime.use_case.GetLatestWatchingAnimeUseCase
import com.brbx.home.common.HomeStrings
import com.brbx.ui_compose.common.toBrbxIcon
import com.brbx.ui_compose.common.toBrbxText
import dev.chiksmedina.solar.BoldSolar
import dev.chiksmedina.solar.bold.ArrowsAction
import dev.chiksmedina.solar.bold.arrowsaction.UndoLeft

internal class LatestWatchedTileInteractorImpl(
    private val latestWatchingAnimeUseCase: GetLatestWatchingAnimeUseCase,
) : LatestWatchedTileInteractor {

    override suspend fun getTile(): DefaultTile? =
        latestWatchingAnimeUseCase()?.let(::createLatestWatchingTile)

    private fun createLatestWatchingTile(
        anime: DomainLatestWatchingAnime,
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
