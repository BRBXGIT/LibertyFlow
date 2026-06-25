package com.brbx.home.view_model

import com.brbx.common.view_model.LibertyFlowViewModel
import com.brbx.home.view_model.model.Intent
import com.brbx.home.view_model.model.State
import com.brbx.home.view_model.processor.processorsModule
import com.brbx.home.view_model.view_model.ViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val viewModelModule = module {
    singleOf(constructor = ::ViewModel) { bind<LibertyFlowViewModel<State, Intent>>() }

    includes(processorsModule)
}