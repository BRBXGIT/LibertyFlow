package com.brbx.preferences.player.manager.autoplay

import com.brbx.preferences.base.DataStoreQualifier
import org.koin.dsl.module

internal val playerAutoplayModule = module {
    single<PlayerAutoplayPrefsManager> {
        PlayerAutoplayPrefsManagerImpl(
            dataStore = get(qualifier = DataStoreQualifier.Player),
        )
    }
}