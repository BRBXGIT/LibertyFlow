package com.brbx.home.view_model.processor.tile.interactor

import com.brbx.home.view_model.processor.tile.interactor.latest_watched.LatestWatchedTileInteractor
import com.brbx.home.view_model.processor.tile.interactor.latest_watched.LatestWatchedTileInteractorImpl
import com.brbx.home.view_model.processor.tile.interactor.stub.StubTileInteractor
import com.brbx.home.view_model.processor.tile.interactor.stub.StubTileInteractorImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val tileInteractorsModule = module {
    singleOf(constructor = ::StubTileInteractorImpl) { bind<StubTileInteractor>() }
    singleOf(constructor = ::LatestWatchedTileInteractorImpl) { bind<LatestWatchedTileInteractor>() }
}