package com.brbx.network.account_user.auth.api

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val accountUserAuthApiModule = module {
    singleOf(constructor = ::AccountUserAuthApiImpl) { bind<AccountUserAuthApi>() }
}