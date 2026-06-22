package com.brbx.preferences.player.manager

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.brbx.preferences.base.DataStoreQualifier
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private const val PlayerStoreName = "liberty_flow_player_store"

private val Context.playerStore by preferencesDataStore(PlayerStoreName)

internal val playerStoreModule = module {
    single(qualifier = DataStoreQualifier.Player) {
        androidContext().playerStore
    }
}