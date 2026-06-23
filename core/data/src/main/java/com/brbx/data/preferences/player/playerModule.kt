package com.brbx.data.preferences.player

import com.brbx.data.preferences.player.autoplay.repository.playerAutoplayRepositoryModule
import com.brbx.data.preferences.player.fullscreen.repository.playerFullscreenRepositoryModule
import com.brbx.data.preferences.player.opening.repository.playerOpeningRepositoryModule
import com.brbx.data.preferences.player.quality.repositry.playerQualityRepositoryModule
import org.koin.dsl.module

internal val playerModule = module {
    includes(
        playerAutoplayRepositoryModule,
        playerFullscreenRepositoryModule,
        playerQualityRepositoryModule,
        playerOpeningRepositoryModule,
    )
}