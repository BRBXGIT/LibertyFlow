package com.brbx.data.user.log_out.repository

import com.brbx.domain.user.log_out.repository.UserLogOutRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val userLogOutRepositoryModule = module {
    singleOf(constructor = ::UserLogOutRepositoryImpl) { bind<UserLogOutRepository>() }
}