package com.brbx.home.view_model

import com.brbx.home.view_model.processor.processorsModule
import com.brbx.home.view_model.view_model.ViewModel
import com.brbx.home.view_model.view_model.ViewModelImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val viewModelImplModule = module {
    singleOf(constructor = ::ViewModelImpl) { bind<ViewModel>() }

    includes(processorsModule)
}