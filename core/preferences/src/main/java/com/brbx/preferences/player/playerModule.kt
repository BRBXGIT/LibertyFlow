package com.brbx.preferences.player

import com.brbx.preferences.player.manager.autoplay.playerAutoplayModule
import com.brbx.preferences.player.manager.fullscreen.playerFullScreenModule
import com.brbx.preferences.player.manager.opening.playerOpeningModule
import com.brbx.preferences.player.manager.playerStoreModule
import com.brbx.preferences.player.manager.quality.playerQualityModule
import org.koin.dsl.module

internal val playerModule = module {
    includes(
        playerStoreModule,
        playerAutoplayModule,
        playerFullScreenModule,
        playerOpeningModule,
        playerQualityModule,
    )
}