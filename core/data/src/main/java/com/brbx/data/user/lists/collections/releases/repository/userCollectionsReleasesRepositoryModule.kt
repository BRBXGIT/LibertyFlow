package com.brbx.data.user.lists.collections.releases.repository

import com.brbx.domain.user.lists.collections.collections_releases.repository.UserCollectionsReleasesRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val userCollectionsReleasesRepositoryModule = module {
    singleOf(constructor = ::UserCollectionsReleasesRepositoryImpl) {
        bind<UserCollectionsReleasesRepository>()
    }
}