package com.brbx.data.network.user.log_out.repository

import com.brbx.domain.network.user.log_out.repository.UserLogOutRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val userLogOutRepositoryModule = module {
    singleOf(constructor = ::UserLogOutRepositoryImpl) { bind<UserLogOutRepository>() }
}