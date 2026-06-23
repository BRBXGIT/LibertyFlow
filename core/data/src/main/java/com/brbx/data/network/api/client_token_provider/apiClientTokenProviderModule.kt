package com.brbx.data.network.api.client_token_provider

import com.brbx.network.base.client.ApiClientTokenProvider
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val apiClientTokenProviderModule = module {
    singleOf(constructor = ::ApiClientTokenProviderImpl) { bind<ApiClientTokenProvider>() }
}