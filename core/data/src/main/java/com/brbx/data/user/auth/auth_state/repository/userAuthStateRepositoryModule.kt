package com.brbx.data.user.auth.auth_state.repository

import com.brbx.domain.user.auth_state.repository.UserAuthStateRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val userAuthStateRepositoryModule = module {
    singleOf(constructor = ::UserAuthStateRepositoryImpl) { bind<UserAuthStateRepository>() }
}