package com.brbx.data.preferences

import com.brbx.data.preferences.appearance.appearanceModule
import com.brbx.data.preferences.player.playerModule
import org.koin.dsl.module

internal val preferencesModule = module {
    includes(
        appearanceModule,
        playerModule,
    )
}