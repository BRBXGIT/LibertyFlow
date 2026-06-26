package com.brbx.common

import com.brbx.common.view_model.viewModelModule
import org.koin.dsl.module

val featureCommonModule = module {
    includes(
        viewModelModule,
    )
}