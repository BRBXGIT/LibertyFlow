package com.brbx.data.network.user.auth

import com.brbx.data.network.user.auth.auth_state.repository.userAuthStateRepositoryModule
import com.brbx.data.network.user.auth.auth.repository.userAuthRepositoryModule
import org.koin.dsl.module

internal val authModule = module {
    includes(
        userAuthStateRepositoryModule,
        userAuthRepositoryModule,
    )
}