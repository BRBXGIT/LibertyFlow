package com.brbx.common.view_model

import com.brbx.common.view_model.processor.processorsModule
import org.koin.dsl.module

internal val viewModelModule = module {
    includes(
        processorsModule,
    )
}