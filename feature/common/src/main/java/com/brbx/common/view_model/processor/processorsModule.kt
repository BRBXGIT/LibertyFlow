package com.brbx.common.view_model.processor

import com.brbx.common.view_model.processor.loading.loadingProcessorModule
import com.brbx.common.view_model.processor.paging.pagingProcessorModule
import com.brbx.common.view_model.processor.search.searchProcessorModule
import org.koin.dsl.module

internal val processorsModule = module {
    includes(
        searchProcessorModule,
        loadingProcessorModule,
        pagingProcessorModule,
    )
}