package com.brbx.home.view_model.processor.random_anime

import com.brbx.common.dispatchers.getDispatcherIo
import org.koin.dsl.module

internal val randomAnimeProcessorModule = module {
    single<RandomAnimeProcessor> {
        RandomAnimeProcessorImpl(
            dispatcherIo = getDispatcherIo(),
            randomAnimeUseCase = get(),
        )
    }
}