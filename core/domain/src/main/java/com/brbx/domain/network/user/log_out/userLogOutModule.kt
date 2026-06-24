package com.brbx.domain.network.user.log_out

import com.brbx.domain.network.user.log_out.use_case.userLogOutUseCaseModule
import org.koin.dsl.module

internal val userLogOutModule = module {
    includes(
        userLogOutUseCaseModule,
    )
}