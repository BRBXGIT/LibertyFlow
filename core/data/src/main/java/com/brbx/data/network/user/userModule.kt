package com.brbx.data.network.user

import com.brbx.data.network.user.auth.authModule
import com.brbx.data.network.user.lists.listsModule
import com.brbx.data.network.user.log_out.repository.userLogOutRepositoryModule
import org.koin.dsl.module

internal val userModule = module {
    includes(
        authModule,
        userLogOutRepositoryModule,
        listsModule,
    )
}