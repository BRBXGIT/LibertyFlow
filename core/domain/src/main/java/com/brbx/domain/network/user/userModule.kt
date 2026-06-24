package com.brbx.domain.network.user

import com.brbx.domain.network.user.auth.use_case.userAuthUseCaseModule
import com.brbx.domain.network.user.auth.userAuthModule
import com.brbx.domain.network.user.auth_state.userAuthStateModule
import com.brbx.domain.network.user.lists.userListsModule
import com.brbx.domain.network.user.log_out.use_case.userLogOutUseCaseModule
import com.brbx.domain.network.user.log_out.userLogOutModule
import org.koin.dsl.module

internal val userModule = module {
    includes(
        userAuthModule,
        userLogOutModule,
        userListsModule,
        userAuthStateModule,
    )
}