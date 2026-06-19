package com.brbx.network.account_user.log_out

import com.brbx.network.account_user.log_out.api.AccountUserLogOutApi
import com.brbx.network.account_user.log_out.api.AccountUserLogOutApiImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val accountUserLogOutApiModule = module {
    singleOf(constructor = ::AccountUserLogOutApiImpl) { bind<AccountUserLogOutApi>() }
}