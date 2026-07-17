package com.brbx.home.view_model.processor.filters

import com.brbx.common.dispatchers.getDispatcherIo
import org.koin.dsl.module

internal val filtersProcessorModule = module {
    single<FiltersProcessor> {
        FiltersProcessorImpl(
            genresUseCase = get(),
            dispatcherIo = getDispatcherIo(),
        )
    }
}