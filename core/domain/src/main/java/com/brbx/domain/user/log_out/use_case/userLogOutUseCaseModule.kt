package com.brbx.domain.user.log_out.use_case

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val userLogOutUseCaseModule = module {
    factoryOf(constructor = ::UserLogOutUseCase)
}