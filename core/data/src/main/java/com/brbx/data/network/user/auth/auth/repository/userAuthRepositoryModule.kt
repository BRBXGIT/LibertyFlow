package com.brbx.data.network.user.auth.auth.repository

import com.brbx.domain.network.user.auth.repository.UserAuthRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val userAuthRepositoryModule = module {
    singleOf(constructor = ::UserAuthRepositoryImpl) { bind<UserAuthRepository>() }
}