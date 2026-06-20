package com.brbx.domain.user

import com.brbx.domain.user.auth.use_case.userAuthUseCaseModule
import com.brbx.domain.user.lists.userListsModule
import com.brbx.domain.user.log_out.use_case.userLogOutUseCaseModule
import org.koin.dsl.module

internal val userModule = module {
    includes(
        userAuthUseCaseModule,
        userLogOutUseCaseModule,
        userListsModule,
    )
}