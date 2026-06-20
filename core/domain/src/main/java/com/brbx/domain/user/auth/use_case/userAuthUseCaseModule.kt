package com.brbx.domain.user.auth.use_case

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val userAuthUseCaseModule = module {
    factoryOf(constructor = ::UserAuthUseCase)
}