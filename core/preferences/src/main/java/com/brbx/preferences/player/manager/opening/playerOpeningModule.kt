package com.brbx.preferences.player.manager.opening

import com.brbx.preferences.base.DataStoreQualifier
import org.koin.dsl.module

internal val playerOpeningModule = module {
    single<PlayerOpeningPrefsManager> {
        PlayerOpeningPrefsManagerImpl(
            dataStore = get(qualifier = DataStoreQualifier.Player)
        )
    }
}