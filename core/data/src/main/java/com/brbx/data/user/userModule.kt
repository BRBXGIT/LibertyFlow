package com.brbx.data.user

import com.brbx.data.user.auth.repository.userAuthRepositoryModule
import com.brbx.data.user.lists.collections.collectionsModule
import com.brbx.data.user.lists.favorites.favoritesModule
import com.brbx.data.user.log_out.repository.userLogOutRepositoryModule
import org.koin.dsl.module

internal val userModule = module {
    includes(
        userAuthRepositoryModule,
        userLogOutRepositoryModule,
        favoritesModule,
        collectionsModule,
    )
}