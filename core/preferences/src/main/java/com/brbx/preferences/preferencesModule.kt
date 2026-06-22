package com.brbx.preferences

import com.brbx.preferences.appearance.appearanceModule
import com.brbx.preferences.auth.authModule
import com.brbx.preferences.player.playerModule
import org.koin.dsl.module

val preferencesModule = module {
    includes(
        authModule,
        appearanceModule,
        playerModule,
    )
}