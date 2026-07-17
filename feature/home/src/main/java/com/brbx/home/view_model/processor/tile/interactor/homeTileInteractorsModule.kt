package com.brbx.home.view_model.processor.tile.interactor

import com.brbx.home.view_model.processor.tile.interactor.latest_watched.LatestWatchedTileInteractor
import com.brbx.home.view_model.processor.tile.interactor.latest_watched.LatestWatchedTileInteractorImpl
import com.brbx.home.view_model.processor.tile.interactor.stub.HomeStubTileInteractor
import com.brbx.home.view_model.processor.tile.interactor.stub.HomeStubTileInteractorImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val homeTileInteractorsModule = module {
    singleOf(constructor = ::HomeStubTileInteractorImpl) { bind<HomeStubTileInteractor>() }
    singleOf(constructor = ::LatestWatchedTileInteractorImpl) { bind<LatestWatchedTileInteractor>() }
}