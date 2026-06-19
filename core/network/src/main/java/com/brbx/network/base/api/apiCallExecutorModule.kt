package com.brbx.network.base.api

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val apiCallExecutorModule = module {
    singleOf(constructor = ::ApiCallExecutorImpl) { bind<ApiCallExecutor>() }
}