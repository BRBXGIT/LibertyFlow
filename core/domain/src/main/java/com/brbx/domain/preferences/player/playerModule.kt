package com.brbx.domain.preferences.player

import com.brbx.domain.preferences.player.autoplay.use_case.playerAutoplayUseCaseModule
import com.brbx.domain.preferences.player.fullscreen.use_case.playerFullScreenUseCaseModule
import com.brbx.domain.preferences.player.opening.use_case.playerOpeningUseCaseModule
import com.brbx.domain.preferences.player.quality.use_case.playerQualityUseCaseModule
import org.koin.dsl.module

internal val playerModule = module {
    includes(
        playerAutoplayUseCaseModule,
        playerFullScreenUseCaseModule,
        playerQualityUseCaseModule,
        playerOpeningUseCaseModule,
    )
}