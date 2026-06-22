package com.brbx.preferences.appearance.manager

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.brbx.preferences.base.DataStoreQualifier
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private const val AppearanceStoreName = "liberty_flow_appearance_store"

private val Context.appearanceStore by preferencesDataStore(AppearanceStoreName)

internal val appearanceStoreModule = module {
    single(qualifier = DataStoreQualifier.Appearance) {
        androidContext().appearanceStore
    }
}