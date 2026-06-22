package com.brbx.data.api

import com.brbx.data.api.client_token_provider.apiClientTokenProviderModule
import org.koin.dsl.module

internal val apiModule = module {
    includes(
        apiClientTokenProviderModule,
    )
}