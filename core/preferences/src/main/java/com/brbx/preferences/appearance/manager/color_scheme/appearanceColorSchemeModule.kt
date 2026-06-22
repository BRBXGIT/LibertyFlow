package com.brbx.preferences.appearance.manager.color_scheme

import com.brbx.preferences.base.DataStoreQualifier
import org.koin.dsl.module

internal val appearanceColorSchemeModule = module {
    single<AppearanceColorSchemePrefsManager> {
        AppearanceColorSchemePrefsManagerImpl(
            dataStore = get(qualifier = DataStoreQualifier.Appearance),
        )
    }
}