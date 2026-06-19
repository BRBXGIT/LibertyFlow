package com.brbx.network.account_user

import com.brbx.network.account_user.auth.accountUserAuthApiModule
import com.brbx.network.account_user.lists.accountUserListsModule
import org.koin.dsl.module

internal val accountUserModule = module {
    includes(
        accountUserAuthApiModule,
        accountUserListsModule,
    )
}