package com.brbx.domain

import com.brbx.domain.network.networkModule
import com.brbx.domain.preferences.preferencesModule
import org.koin.dsl.module

val domainModule = module {
    includes(
        networkModule,
        preferencesModule,
    )
}