package com.brbx.preferences.player.manager.fullscreen

import com.brbx.preferences.base.DataStoreQualifier
import org.koin.dsl.module

internal val playerFullScreenModule = module {
    single<PlayerFullScreenPrefsManager> {
        PlayerFullScreenPrefsManagerImpl(
            dataStore = get(qualifier = DataStoreQualifier.Player)
        )
    }
}