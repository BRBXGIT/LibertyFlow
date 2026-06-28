package com.brbx.libertyflow.app

import android.app.Application
import com.brbx.common.coreCommonModule
import com.brbx.common.featureCommonModule
import com.brbx.data.dataModule
import com.brbx.domain.domainModule
import com.brbx.home.homeModule
import com.brbx.local_dbs.localDbsModule
import com.brbx.network.networkModule
import com.brbx.preferences.preferencesModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

internal class KoinApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@KoinApp)
            modules(
                // Core
                networkModule,
                preferencesModule,
                localDbsModule,
                coreCommonModule,
                dataModule,
                domainModule,
                // Feature
                featureCommonModule,
                homeModule,
            )
        }
    }
}