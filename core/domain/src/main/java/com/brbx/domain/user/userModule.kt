package com.brbx.domain.user

import com.brbx.domain.user.auth.use_case.userAuthUserCaseModule
import com.brbx.domain.user.log_out.use_case.userLogOutUseCaseModule
import org.koin.dsl.module

internal val userModule = module {
    includes(
        userAuthUserCaseModule,
        userLogOutUseCaseModule,
    )
}