package com.example.common.dispatchers

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val libertyFlowDispatcher: LibertyFlowDispatcher)

enum class LibertyFlowDispatcher {
    Default,
    IO,
    Main
}