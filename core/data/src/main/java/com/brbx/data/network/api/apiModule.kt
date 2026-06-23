package com.brbx.data.network.api

import com.brbx.data.network.api.client_token_provider.apiClientTokenProviderModule
import com.brbx.data.network.api.executor.apiCallExecutorModule
import org.koin.dsl.module

internal val apiModule = module {
    includes(
        apiClientTokenProviderModule,
        apiCallExecutorModule,
    )
}