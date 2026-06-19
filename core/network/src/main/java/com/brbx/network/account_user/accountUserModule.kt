package com.brbx.network.account_user

import com.brbx.network.account_user.auth.api.accountUserAuthApiModule
import org.koin.dsl.module

internal val accountUserModule = module {
    includes(
        accountUserAuthApiModule,
    )
}