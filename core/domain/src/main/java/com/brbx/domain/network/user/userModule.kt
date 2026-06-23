package com.brbx.domain.network.user

import com.brbx.domain.network.user.auth.use_case.userAuthUseCaseModule
import com.brbx.domain.network.user.lists.userListsModule
import com.brbx.domain.network.user.log_out.use_case.userLogOutUseCaseModule
import org.koin.dsl.module

internal val userModule = module {
    includes(
        userAuthUseCaseModule,
        userLogOutUseCaseModule,
        userListsModule,
    )
}