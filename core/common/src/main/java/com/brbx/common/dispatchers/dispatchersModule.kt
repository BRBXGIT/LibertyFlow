package com.brbx.common.dispatchers

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.scope.Scope
import org.koin.dsl.module

internal val dispatchersModule = module {
    single(qualifier = DispatcherQualifier.Io) { Dispatchers.IO }
    single(qualifier = DispatcherQualifier.Default) { Dispatchers.Default }
    single(qualifier = DispatcherQualifier.Main) { Dispatchers.Main }
}

fun Scope.getDispatcherIo(): CoroutineDispatcher =
    get(qualifier = DispatcherQualifier.Io)

fun Scope.getDispatcherDefault(): CoroutineDispatcher =
    get(qualifier = DispatcherQualifier.Default)

fun Scope.getDispatcherMain(): CoroutineDispatcher =
    get(qualifier = DispatcherQualifier.Main)