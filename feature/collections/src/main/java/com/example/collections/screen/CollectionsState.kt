package com.example.collections.screen

import androidx.compose.runtime.Immutable
import com.example.common.vm_helpers.models.AuthState
import com.example.common.vm_helpers.models.SearchForm
import com.example.data.models.common.request.request_parameters.Collection

/**
 * Represents the complete UI state for the Collections screen.
 * * This data class is marked as [Immutable] to allow Compose to optimize recompositions.
 * It encapsulates authentication status, error states, and form-specific sub-states.
 *
 * @property authState The sub-state managing the authentication BottomSheet, credentials and current AuthState.
 * @property searchForm The sub-state managing search queries and active search status.
 * @property selectedCollection The currently active collection tab (e.g., 'WATCHING', 'PLANNED').
 */
@Immutable
data class CollectionsState(
    val authState: AuthState = AuthState(),

    val searchForm: SearchForm = SearchForm(),

    val selectedCollection: Collection = Collection.WATCHING
)
