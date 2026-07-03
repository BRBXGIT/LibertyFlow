package com.brbx.common.view_model.processor.tile.processor

import arrow.optics.Lens
import com.brbx.common.model.alias.CommonStrings
import com.brbx.common.view_model.processor.tile.model.CommonTileIntent
import com.brbx.common.view_model.processor.tile.model.CommonTileState
import com.brbx.common.view_model.processor.tile.model.TileType
import com.brbx.common.view_model.view_model.LibertyFlowMviScope
import com.brbx.domain.local_dbs.watching_anime.use_case.anime.use_case.GetLatestWatchingAnimeUseCase
import com.brbx.ui_compose.common.toBrbxIcon
import com.brbx.ui_compose.common.toBrbxText
import dev.chiksmedina.solar.BoldSolar
import dev.chiksmedina.solar.bold.Arrows
import dev.chiksmedina.solar.bold.ArrowsAction
import dev.chiksmedina.solar.bold.DesignTools
import dev.chiksmedina.solar.bold.arrows.RoundArrowRight
import dev.chiksmedina.solar.bold.arrowsaction.UndoLeft
import dev.chiksmedina.solar.bold.designtools.Pallete2
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

internal class CommonTileProcessorImpl<State>(
    private val tileLens: Lens<State, CommonTileState>,
    private val getLatestWatchingAnimeUseCase: GetLatestWatchingAnimeUseCase,
    private val dispatcherIo: CoroutineDispatcher,
) : CommonTileProcessor<State> {

    override fun LibertyFlowMviScope<State>.process(intent: CommonTileIntent) {
        when (intent) {
            is CommonTileIntent.GetTile -> {
                coroutineScope.launch(context = dispatcherIo) {
                    val tile = getTileByType(intent.type)
                    updateState {
                        tileLens.modify(source = this) {
                            it.copy(
                                title = tile.title,
                                description = tile.description,
                                icon = tile.icon,
                                precollection = tile.precollection,
                            )
                        }
                    }
                }
            }
            CommonTileIntent.TogglePrecollectionVisibility -> {
                updateState {
                    tileLens.modify(source = this) {
                        it.copy(isPrecollectionVisible = !it.isPrecollectionVisible)
                    }
                }
            }
        }
    }

    // TODO Rewrite to interactors
    private suspend fun getTileByType(type: TileType): CommonTileState =
        when (type) {
            TileType.Episode.LatestWatched -> {
                val latest = getLatestWatchingAnimeUseCase()
                latest?.let { anime ->
                    CommonTileState(
                        type = type,
                        title = anime.title.toBrbxText(),
                        description = CommonStrings.user_watched_tile_description.toBrbxText(latest.lastEpisodeIndex),
                        icon = BoldSolar.ArrowsAction.UndoLeft.toBrbxIcon(),
                        precollection = CommonTileState.Precollection(
                            label = CommonStrings.user_watched_tile_precollection_title.toBrbxText(),
                            icon = BoldSolar.Arrows.RoundArrowRight.toBrbxIcon(),
                        ),
                        isPrecollectionVisible = false,
                    )
                } ?: getTileByType(TileType.Stub.Theme)
            }
            TileType.Stub.Theme -> {
                CommonTileState(
                    type = type,
                    title = CommonStrings.theme_tile_title.toBrbxText(),
                    description = CommonStrings.theme_tile_description.toBrbxText(),
                    icon = BoldSolar.DesignTools.Pallete2.toBrbxIcon(),
                    precollection = CommonTileState.Precollection(
                        label = CommonStrings.theme_tile_precollection_text.toBrbxText(),
                        icon = BoldSolar.Arrows.RoundArrowRight.toBrbxIcon(),
                    ),
                    isPrecollectionVisible = false,
                )
            }
        }
}