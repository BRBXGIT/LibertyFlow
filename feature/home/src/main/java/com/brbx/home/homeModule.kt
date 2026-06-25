package com.brbx.home

import com.brbx.home.view_model.viewModelImplModule
import org.koin.dsl.module

val homeModule = module {
    includes(viewModelImplModule)
}