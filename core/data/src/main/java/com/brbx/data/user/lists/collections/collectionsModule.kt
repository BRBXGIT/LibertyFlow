package com.brbx.data.user.lists.collections

import com.brbx.data.user.lists.collections.interactor.collectionsIdsInteractorModule
import org.koin.dsl.module

internal val collectionsModule = module {
    includes(
        collectionsIdsInteractorModule,
    )
}