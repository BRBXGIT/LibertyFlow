package com.brbx.preferences.appearance.manager.folder_style

import com.brbx.preferences.base.DataStoreQualifier
import org.koin.dsl.module

internal val appearanceFolderStyleModule = module {
    single<AppearanceFolderStylePrefsManager> {
        AppearanceFolderStylePrefsManagerImpl(
            dataStore = get(qualifier = DataStoreQualifier.Appearance)
        )
    }
}