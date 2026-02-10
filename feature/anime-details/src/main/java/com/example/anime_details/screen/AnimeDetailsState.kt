package com.example.anime_details.screen

import androidx.compose.runtime.Immutable
import com.example.common.ui_helpers.auth.AuthFormState
import com.example.common.ui_helpers.loading_state.LoadingState
import com.example.data.models.auth.AuthState
import com.example.data.models.collections.collection.AnimeCollection
import com.example.data.models.releases.anime_details.AnimeDetails
import com.example.data.models.common.request.request_parameters.Collection

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

    // Collections
    val collectionsState: CollectionsState = CollectionsState(),

    // Auth Bottom Sheet & Form (Grouped to isolate frequent updates)
    val authForm: AuthFormState = AuthFormState(),

    // UI Toggles
    val isDescriptionExpanded: Boolean = false,
) {
    // --- Nested States ---
    @Immutable
    data class FavoritesState(
        val loadingState: LoadingState = LoadingState(),
        val ids: List<Int> = emptyList()
    )

    @Immutable
    data class CollectionsState(
        val loadingState: LoadingState = LoadingState(),
        val collections: List<AnimeCollection> = emptyList()
    )

    // Auth
    fun updateAuthForm(transformer: (AuthFormState) -> AuthFormState): AnimeDetailsState {
        return copy(authForm = transformer(authForm))
    }

    // Favorites
    fun addAnimeToFavorites(): AnimeDetailsState {
        val animeId = anime?.id ?: return this
        return copy(favoritesState = favoritesState.copy(ids = favoritesState.ids + animeId, loadingState = loadingState.withLoading(false)))
    }

    fun removeAnimeFromFavorites(): AnimeDetailsState {
        val animeId = anime?.id ?: return this
        return copy(favoritesState = favoritesState.copy(ids = favoritesState.ids - animeId, loadingState = loadingState.withLoading(false)))
    }

    // Collections
    val activeCollection: Collection?
        get() = collectionsState.collections.firstOrNull { col ->
            anime?.id in col.ids
        }?.collection

    fun updateCollection(collection: Collection, isAdded: Boolean): AnimeDetailsState {
        val animeId = anime?.id ?: return this
        val updatedCollections = collectionsState.collections.map { col ->
            if (col.collection == collection) {
                val newIds = if (isAdded) col.ids + animeId else col.ids - animeId
                col.copy(ids = newIds)
            } else col
        }
        return copy(
            collectionsState = collectionsState.copy(
                collections = updatedCollections,
                loadingState = collectionsState.loadingState.withLoading(false)
            )
        )
    }
}
