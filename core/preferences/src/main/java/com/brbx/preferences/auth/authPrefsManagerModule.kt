package com.brbx.preferences.auth

import com.brbx.preferences.base.DataStoreQualifier
import org.koin.dsl.module

internal val authPrefsManagerModule = module {
    single<AuthPrefsManager> {
        AuthPrefsManagerImpl(
            dataStore = get(qualifier = DataStoreQualifier.Auth),
        )
    }
}