package com.example.local.onboarding

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private const val DATASTORE_NAME = "liberty_fow_onboarding_prefs"
private val Context.dataStore by preferencesDataStore(DATASTORE_NAME)

@Singleton
class OnboardingManagerImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
): OnboardingManager {

    private companion object {
        private val IS_ONBOARDING_COMPLETED_KEY = booleanPreferencesKey("is_onboarding_completed_key")
    }

    override val isOnboardingCompleted = context.dataStore.data
        .map { preferences -> preferences[IS_ONBOARDING_COMPLETED_KEY] }

    override suspend fun saveOnboardingCompleted() {
        context.dataStore.edit { preferences ->
            preferences[IS_ONBOARDING_COMPLETED_KEY] = true
        }
    }
}