package com.brbx.preferences.base

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal abstract class BasePrefsManager(
    private val dataStore: DataStore<Preferences>
) {
    protected fun <T> getValue(key: Preferences.Key<T>): Flow<T?> =
        dataStore.data.map { preferences -> preferences[key] }

    protected suspend fun <T> setValue(key: Preferences.Key<T>, value: T?) {
        if (value == null) {
            clearValue(key)
        } else {
            dataStore.edit { preferences -> preferences[key] = value }
        }
    }

    protected suspend fun <T> clearValue(key: Preferences.Key<T>) {
        dataStore.edit { preferences -> preferences.remove(key) }
    }
}