package com.brbx.preferences.player.manager.quality

import com.brbx.preferences.base.DataStoreQualifier
import org.koin.dsl.module

internal val playerQualityModule = module {
    single<PlayerQualityPrefsManager> {
        PlayerQualityPrefsManagerImpl(
            dataStore = get(qualifier = DataStoreQualifier.Player)
        )
    }
}