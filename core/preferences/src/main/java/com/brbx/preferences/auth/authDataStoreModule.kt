package com.brbx.preferences.auth

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.brbx.preferences.base.DataStoreQualifier
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private const val AuthStoreName = "liberty_flow_auth_store"

private val Context.authStore by preferencesDataStore(AuthStoreName)

internal val authDataStoreModule = module {
    single(qualifier = DataStoreQualifier.Auth) {
        androidContext().authStore
    }
}