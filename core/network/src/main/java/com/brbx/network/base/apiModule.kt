package com.brbx.network.base

import com.brbx.network.base.api.apiCallExecutorModule
import com.brbx.network.base.client.apiClientModule
import org.koin.dsl.module

internal val apiModule = module {
    includes(
        apiClientModule,
        apiCallExecutorModule,
    )
}