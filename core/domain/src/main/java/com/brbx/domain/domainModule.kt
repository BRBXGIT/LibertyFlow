package com.brbx.domain

import com.brbx.domain.user.userModule
import org.koin.dsl.module

val domainModule = module {
    includes(
        userModule,
    )
}