package com.brbx.common.dispatchers

import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val dispatchersModule = module {
    single(qualifier = DispatcherQualifier.Io) { Dispatchers.IO }
    single(qualifier = DispatcherQualifier.Default) { Dispatchers.Default }
    single(qualifier = DispatcherQualifier.Main) { Dispatchers.Main }
}