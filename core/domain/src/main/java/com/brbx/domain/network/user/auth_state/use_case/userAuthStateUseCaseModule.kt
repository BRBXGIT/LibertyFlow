package com.brbx.domain.network.user.auth_state.use_case

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val userAuthStateUseCaseModule = module {
    factoryOf(constructor = ::GetUserAuthStateUseCase)
}