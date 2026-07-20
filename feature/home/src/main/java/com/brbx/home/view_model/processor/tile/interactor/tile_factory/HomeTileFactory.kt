package com.brbx.home.view_model.processor.tile.interactor.tile_factory

import com.brbx.common.model.common.model.DefaultTile
import com.brbx.domain.local_dbs.watching_anime.use_case.anime.model.DomainLatestWatchingAnime

internal interface HomeTileFactory {
    fun createThemeTile(): DefaultTile
    fun createAniLibertyTile(): DefaultTile
    fun createLatestWatchingTile(anime: DomainLatestWatchingAnime): DefaultTile
}