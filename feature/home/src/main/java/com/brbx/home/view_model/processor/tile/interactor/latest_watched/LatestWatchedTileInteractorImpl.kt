package com.brbx.home.view_model.processor.tile.interactor.latest_watched

import com.brbx.common.model.common.model.DefaultTile
import com.brbx.domain.local_dbs.watching_anime.use_case.anime.use_case.GetLatestWatchingAnimeUseCase
import com.brbx.home.view_model.processor.tile.interactor.tile_factory.HomeTileFactory

internal class LatestWatchedTileInteractorImpl(
    private val latestWatchingAnimeUseCase: GetLatestWatchingAnimeUseCase,
    private val tileFactory: HomeTileFactory,
) : LatestWatchedTileInteractor {

    override suspend fun getTile(): DefaultTile? =
        latestWatchingAnimeUseCase()?.let { anime ->
            tileFactory.createLatestWatchingTile(anime)
        }
}
