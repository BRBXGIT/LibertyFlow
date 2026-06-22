package com.brbx.data.user.lists.collections.interactor

import org.koin.core.module.dsl.binds
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val collectionsIdsInteractorModule = module {
    singleOf(constructor = ::CollectionsIdsInteractorImpl) {
        binds(classes = listOf(CollectionsIdsInteractor::class, CollectionsIdsSource::class))
    }
}