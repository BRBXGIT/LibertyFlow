package com.brbx.domain.network.user.auth_state

import com.brbx.domain.network.user.auth_state.use_case.userAuthStateUseCaseModule
import org.koin.dsl.module

internal val userAuthStateModule = module {
    includes(
        userAuthStateUseCaseModule,
    )
}