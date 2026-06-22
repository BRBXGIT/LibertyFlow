package com.brbx.preferences.appearance.manager.theme

import com.brbx.preferences.base.DataStoreQualifier
import org.koin.dsl.module

internal val appearanceThemeModule = module {
    single<AppearanceThemePrefsManager> {
        AppearanceThemePrefsManagerImpl(
            dataStore = get(qualifier = DataStoreQualifier.Appearance),
        )
    }
}