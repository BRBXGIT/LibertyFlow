package com.brbx.home.view_model.processor.tile_processor

import arrow.optics.copy
import com.brbx.common.view_model.view_model.LibertyFlowMviScope
import com.brbx.design_system.component.precollection.PrecollectionModel
import com.brbx.design_system.component.tile.TileModel
import com.brbx.design_system.component.tile.isPrecollectionVisible
import com.brbx.domain.local_dbs.watching_anime.use_case.anime.model.DomainLatestWatchingAnime
import com.brbx.domain.local_dbs.watching_anime.use_case.anime.use_case.GetLatestWatchingAnimeUseCase
import com.brbx.home.common.HomeStrings
import com.brbx.home.view_model.model.Intent
import com.brbx.home.view_model.model.State
import com.brbx.home.view_model.model.latestWatchingAnime
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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

internal class TileProcessorImpl(
    private val latestWatchedAnimeUseCase: GetLatestWatchingAnimeUseCase,
    private val dispatcherIo: CoroutineDispatcher,
) : TileProcessor {

    override fun LibertyFlowMviScope<State>.process(intent: Intent.TileIntent) {
        when (intent) {
            is Intent.TileIntent.GetTile -> {
                coroutineScope.launch(context = dispatcherIo) {
                    val result = latestWatchedAnimeUseCase()?.toUi(
                        onClick = { /* TODO navigate to details */ }
                    ) ?: getThemeTile()
                    updateState { copy { State.latestWatchingAnime set result } }
                }
            }
            is Intent.TileIntent.TogglePrecollectionVisibility -> {
                updateState {
                    copy {
                        State.latestWatchingAnime transform { tile ->
                            tile?.copy { TileModel.isPrecollectionVisible transform { !it } }
                        }
                    }
                }
            }
        }
    }

    private fun DomainLatestWatchingAnime.toUi(onClick: (animeId: Int) -> Unit): TileModel =
        TileModel(
            title = this.title.toBrbxText(),
            description = HomeStrings.user_watched_tile_description.toBrbxText(
                this.lastEpisodeIndex,
            ),
            icon = BoldSolar.ArrowsAction.UndoLeft.toBrbxIcon(),
            onClick = { onClick(this.animeId) },
            precollection = PrecollectionModel(
                text = HomeStrings.user_watched_tile_precollection_title.toBrbxText(),
                icon = BoldSolar.Arrows.RoundArrowRight.toBrbxIcon(),
                onClick = { onClick(this.animeId) }, // TODO Move to ui
            )
        )

    private fun getThemeTile(): TileModel =
        TileModel(
            title = HomeStrings.theme_tile_title.toBrbxText(),
            description = HomeStrings.theme_tile_description.toBrbxText(),
            icon = BoldSolar.DesignTools.Pallete2.toBrbxIcon(),
            onClick = { /* TODO navigate to theme screen */ }, // TODO Move to ui
            precollection = PrecollectionModel(
                text = HomeStrings.theme_tile_precollection_text.toBrbxText(),
                icon = OutlineSolar.Arrows.AltArrowRight.toBrbxIcon(),
                onClick = { /* TODO navigate to theme screen */ },
            )
        )
}