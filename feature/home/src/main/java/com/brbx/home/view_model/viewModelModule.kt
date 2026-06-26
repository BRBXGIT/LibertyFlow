package com.brbx.home.view_model

import com.brbx.common.view_model.processor.search.getCommonSearchProcessor
import com.brbx.home.view_model.model.State
import com.brbx.home.view_model.model.search
import com.brbx.home.view_model.processor.processorsModule
import com.brbx.home.view_model.view_model.ViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

internal val viewModelModule = module {
    viewModel {
        ViewModel(
            searchProcessor = getCommonSearchProcessor(lens = State.search),
            randomAnimeProcessor = get(),
            filtersProcessor = get(),
            catalogProcessor = get(),
            latestWatchingAnimeProcessor = get()
        )
    }

    includes(processorsModule)
}