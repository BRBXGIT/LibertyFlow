package com.brbx.network.api_client

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val apiClientModule = module {
    singleOf(constructor = ::ApiClientProviderImpl) { bind<ApiClientProvider>() }
}