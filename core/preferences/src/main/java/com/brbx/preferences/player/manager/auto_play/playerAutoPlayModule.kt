package com.brbx.preferences.player.manager.auto_play

import com.brbx.preferences.base.DataStoreQualifier
import org.koin.dsl.module

internal val playerAutoPlayModule = module {
    single<PlayerAutoPlayPrefsManager> {
        PlayerAutoPlayPrefsManagerImpl(
            dataStore = get(qualifier = DataStoreQualifier.Player),
        )
    }
}