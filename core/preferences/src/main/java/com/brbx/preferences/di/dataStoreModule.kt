package com.brbx.preferences.di

import com.brbx.preferences.auth.authDataStoreModule
import org.koin.dsl.module

internal val dataStoreModule = module {
    includes(
        authDataStoreModule,
    )
}