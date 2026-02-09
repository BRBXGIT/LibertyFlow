package com.example.anime_details.screen

import androidx.compose.runtime.Immutable
import com.example.common.ui_helpers.auth.AuthFormState
import com.example.common.ui_helpers.loading_state.LoadingState
import com.example.data.models.auth.AuthState
import com.example.data.models.common.request.request_parameters.Collection
import com.example.data.models.releases.anime_details.AnimeDetails

@Immutable
data class AnimeDetailsState(
    // Global Auth Status
    val authState: AuthState = AuthState.LoggedOut,

    // Screen Loading/Error States
    val loadingState: LoadingState = LoadingState(),

    // Domain Data
    val anime: AnimeDetails? = null,
    val watchedEps: List<Int> = emptyList(),

    // Favorites Logic (Grouped for clarity)
    val favoritesState: FavoritesState = FavoritesState(),

    // Auth Bottom Sheet & Form (Grouped to isolate frequent updates)
    val authForm: AuthFormState = AuthFormState(),

    // UI Toggles
    val isDescriptionExpanded: Boolean = false,
) {
    // --- Nested States ---
    @Immutable
    data class FavoritesState(
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val ids: List<Int> = emptyList()
    )

    @Immutable
    data class CollectionsState(
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val ids: List<Map<Collection, List<Int>>> = emptyList()
    )

    // Auth
    fun updateAuthForm(transformer: (AuthFormState) -> AuthFormState): AnimeDetailsState {
        return copy(authForm = transformer(authForm))
    }

    fun updateFavorites(transformer: (FavoritesState) -> FavoritesState): AnimeDetailsState {
        return copy(favoritesState = transformer(favoritesState))
    }

    // Safer collection modification (avoids crash if anime is null)
    fun addAnimeToFavorites(): AnimeDetailsState {
        val animeId = anime?.id ?: return this
        return updateFavorites { it.copy(ids = it.ids + animeId) }
    }

    fun removeAnimeFromFavorites(): AnimeDetailsState {
        val animeId = anime?.id ?: return this
        return updateFavorites { it.copy(ids = it.ids - animeId) }
    }
}
