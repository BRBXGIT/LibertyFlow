package com.example.local.onboarding

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import com.example.local.utils.BasePrefsManager
import com.example.local.utils.DataStoreQualifier
import com.example.local.utils.LibertyFlowDataStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Manages the state of the first-launch onboarding experience.
 */
class OnboardingPrefsManagerImpl @Inject constructor(
    @DataStoreQualifier(LibertyFlowDataStore.Onboarding) dataStore: DataStore<Preferences>
): OnboardingPrefsManager, BasePrefsManager(dataStore) {

    private companion object {
        private val IS_ONBOARDING_COMPLETED_KEY = booleanPreferencesKey("is_onboarding_completed_key")
    }

    /**
     * A [Flow] emitting the onboarding status.
     * Defaults to 'false' if the key hasn't been set yet.
     */
    override val isOnboardingCompleted = getValue(IS_ONBOARDING_COMPLETED_KEY)

    /**
     * Marks the onboarding process as finished.
     */
    override suspend fun saveOnboardingCompleted() =
        setValue(IS_ONBOARDING_COMPLETED_KEY, true)
}