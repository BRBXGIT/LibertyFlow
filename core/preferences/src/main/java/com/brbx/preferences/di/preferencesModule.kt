package com.brbx.preferences.di

import org.koin.dsl.module

val preferencesModule = module {
    includes(
        dataStoreModule,
        prefsManagerModule,
    )
}