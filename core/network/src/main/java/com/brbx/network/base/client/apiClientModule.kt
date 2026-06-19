package com.brbx.network.base.client

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val apiClientModule = module {
    singleOf(constructor = ::ApiClientProviderImpl) { bind<ApiClientProvider>() }
}