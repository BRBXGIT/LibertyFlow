package com.brbx.common.view_model.processor.tile.processor.interactor.episode

import com.brbx.common.model.alias.CommonStrings
import com.brbx.common.view_model.processor.tile.model.CommonTileState
import com.brbx.common.view_model.processor.tile.model.TileType
import com.brbx.common.view_model.processor.tile.processor.interactor.stub.StubTileInteractor
import com.brbx.domain.local_dbs.watching_anime.use_case.anime.use_case.GetLatestWatchingAnimeUseCase
import com.brbx.ui_compose.common.toBrbxIcon
import com.brbx.ui_compose.common.toBrbxText
import dev.chiksmedina.solar.BoldSolar
import dev.chiksmedina.solar.OutlineSolar
import dev.chiksmedina.solar.bold.ArrowsAction
import dev.chiksmedina.solar.bold.arrowsaction.UndoLeft
import dev.chiksmedina.solar.outline.Arrows
import dev.chiksmedina.solar.outline.arrows.AltArrowRight

internal class EpisodeTileInteractorImpl(
    private val latestWatchingUseCase: GetLatestWatchingAnimeUseCase,
    private val stubInteractor: StubTileInteractor,
) : EpisodeTileInteractor {

    override suspend fun getTile(): CommonTileState {
        val latest = latestWatchingUseCase()
        return latest?.let { anime ->
            CommonTileState(
                type = TileType.Episode.LatestWatched,
                title = anime.title.toBrbxText(),
                description = CommonStrings.user_watched_tile_description
                    .toBrbxText(latest.lastEpisodeIndex),
                icon = BoldSolar.ArrowsAction.UndoLeft.toBrbxIcon(),
                precollection = CommonTileState.Precollection(
                    label = CommonStrings.user_watched_tile_precollection_title.toBrbxText(),
                    icon = OutlineSolar.Arrows.AltArrowRight.toBrbxIcon(),
                ),
                isPrecollectionVisible = false,
            )
        } ?: stubInteractor.getTile()
    }
}