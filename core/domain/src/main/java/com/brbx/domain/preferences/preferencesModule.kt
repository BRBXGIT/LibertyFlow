package com.brbx.domain.preferences

import com.brbx.domain.preferences.appearance.appearanceModule
import com.brbx.domain.preferences.player.playerModule
import org.koin.dsl.module

internal val preferencesModule = module {
    includes(
        appearanceModule,
        playerModule,
    )
}