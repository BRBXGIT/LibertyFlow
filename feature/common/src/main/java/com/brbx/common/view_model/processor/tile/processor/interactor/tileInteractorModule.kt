package com.brbx.common.view_model.processor.tile.processor.interactor

import com.brbx.common.view_model.processor.tile.processor.interactor.episode.EpisodeTileInteractor
import com.brbx.common.view_model.processor.tile.processor.interactor.episode.EpisodeTileInteractorImpl
import com.brbx.common.view_model.processor.tile.processor.interactor.recommendation.RecommendationTileInteractor
import com.brbx.common.view_model.processor.tile.processor.interactor.recommendation.RecommendationTileInteractorImpl
import com.brbx.common.view_model.processor.tile.processor.interactor.stub.StubTileInteractor
import com.brbx.common.view_model.processor.tile.processor.interactor.stub.StubTileInteractorImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val tileInteractorModule = module {
    factoryOf(constructor = ::EpisodeTileInteractorImpl) { bind<EpisodeTileInteractor>() }
    factoryOf(constructor = ::RecommendationTileInteractorImpl) {
        bind<RecommendationTileInteractor>()
    }
    factoryOf(constructor = ::StubTileInteractorImpl) { bind<StubTileInteractor>() }
}