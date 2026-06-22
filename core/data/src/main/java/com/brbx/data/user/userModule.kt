package com.brbx.data.user

import com.brbx.data.user.auth.authModule
import com.brbx.data.user.lists.listsModule
import com.brbx.data.user.log_out.repository.userLogOutRepositoryModule
import org.koin.dsl.module

internal val userModule = module {
    includes(
        authModule,
        userLogOutRepositoryModule,
        listsModule,
    )
}