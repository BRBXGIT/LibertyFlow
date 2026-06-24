package com.brbx.domain.network.user.auth

import com.brbx.domain.network.user.auth.use_case.userAuthUseCaseModule
import org.koin.dsl.module

internal val userAuthModule = module {
    includes(
        userAuthUseCaseModule,
    )
}