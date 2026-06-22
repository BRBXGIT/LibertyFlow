package com.brbx.data.user.auth

import com.brbx.data.user.auth.auth_state_repository.userAuthStateRepositoryModule
import com.brbx.data.user.auth.repository.userAuthRepositoryModule
import org.koin.dsl.module

internal val authModule = module {
    includes(
        userAuthStateRepositoryModule,
        userAuthRepositoryModule,
    )
}