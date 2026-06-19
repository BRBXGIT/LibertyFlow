package com.brbx.preferences.di

import com.brbx.preferences.auth.authPrefsManagerModule
import org.koin.dsl.module

internal val prefsManagerModule = module {
    includes(
        authPrefsManagerModule,
    )
}