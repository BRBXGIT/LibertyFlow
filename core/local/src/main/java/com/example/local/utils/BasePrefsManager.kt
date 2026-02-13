package com.example.local.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Base abstraction for Jetpack Preferences DataStore managers.
 * Provides helper methods to simplify boilerplate code for CRUD operations.
 */
abstract class BasePrefsManager(
    private val dataStore: DataStore<Preferences>
) {
    /**
     * Observes a specific preference key as a [Flow].
     */
    protected fun <T> getValue(key: Preferences.Key<T>): Flow<T?> {
        return dataStore.data
            .map { preferences -> preferences[key] }
    }

    /**
     * Persists a value to the DataStore for the given key.
     */
    protected suspend fun <T> setValue(key: Preferences.Key<T>, value: T) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    /**
     * Removes a value from the DataStore for the given key.
     */
    protected suspend fun <T> clearValue(key: Preferences.Key<T>) {
        dataStore.edit { preferences ->
            preferences.remove(key)
        }
    }
}