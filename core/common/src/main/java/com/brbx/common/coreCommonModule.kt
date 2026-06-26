package com.brbx.common

import com.brbx.common.dispatchers.dispatchersModule
import org.koin.dsl.module

val coreCommonModule = module {
    includes(
        dispatchersModule,
    )
}