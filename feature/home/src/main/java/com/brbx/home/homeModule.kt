package com.brbx.home

import com.brbx.home.view_model.viewModelModule
import org.koin.dsl.module

val homeModule = module {
    includes(viewModelModule)
}