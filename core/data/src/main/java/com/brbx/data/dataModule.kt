package com.brbx.data

import com.brbx.data.network.networkModule
import com.brbx.data.preferences.preferencesModule
import org.koin.dsl.module

val dataModule = module {
    includes(
        networkModule,
        preferencesModule,
    )
}