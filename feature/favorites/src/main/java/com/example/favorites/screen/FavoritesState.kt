package com.example.favorites.screen

import androidx.compose.runtime.Immutable
import com.example.common.vm_helpers.auth.models.AuthState
import com.example.common.vm_helpers.filters.models.FiltersState
import com.example.common.vm_helpers.models.LoadingState

/**
 * Represents the complete UI state for the Favorites screen.
 * * This data class is marked as [@Immutable] to optimize Compose recomposition.
 * It encapsulates authentication status, global loading/error states, and
 * specific form states for both login and searching.
 *
 * @property loadingState Handles the visibility of progress indicators and error messages.
 * @property authState State specifically for the authentication BottomSheet and input fields.
 * @property searchForm State managing the search bar visibility and query text.
 */
@Immutable
data class FavoritesState(
    val loadingState: LoadingState = LoadingState(),

    val authState: AuthState = AuthState(),

    val filtersState: FiltersState = FiltersState()
)