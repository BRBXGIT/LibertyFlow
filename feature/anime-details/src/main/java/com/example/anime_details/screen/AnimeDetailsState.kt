package com.example.anime_details.screen

import androidx.compose.runtime.Immutable
import com.example.common.vm_helpers.models.AuthState
import com.example.common.vm_helpers.models.LoadingState
import com.example.data.models.auth.UserAuthState
import com.example.data.models.collections.collection.AnimeCollection
import com.example.data.models.releases.anime_details.AnimeDetails
import com.example.data.models.common.request.request_parameters.Collection

@Immutable
data class AnimeDetailsState(
    // Global Auth Status
    val userAuthState: UserAuthState = UserAuthState.LoggedOut,

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
    val authForm: AuthState = AuthState(),

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
        val collectionBSVisible: Boolean = false,
        val loadingState: LoadingState = LoadingState(isLoading = true),
        val collections: List<AnimeCollection> = emptyList()
    ) {
        fun toggleBS() = copy(collectionBSVisible = !collectionBSVisible)
    }

    // Auth
    fun updateAuthForm(transformer: (AuthState) -> AuthState): AnimeDetailsState {
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
            col.id == anime?.id
        }?.collection

    fun updateCollection(collection: Collection, isAdded: Boolean): AnimeDetailsState {
        val animeId = anime?.id ?: return this
        val currentList = collectionsState.collections.toMutableList()

        currentList.removeAll { it.id == animeId }

        if (isAdded) {
            currentList.add(AnimeCollection(collection = collection, id = animeId))
        }

        return copy(
            collectionsState = collectionsState.copy(
                collections = currentList,
                loadingState = collectionsState.loadingState.withLoading(false)
            )
        )
    }
}
