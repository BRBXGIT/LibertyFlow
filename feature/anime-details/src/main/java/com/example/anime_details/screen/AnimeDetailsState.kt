package com.example.anime_details.screen

import androidx.compose.runtime.Immutable
import com.example.common.vm_helpers.auth.models.AuthState
import com.example.common.vm_helpers.models.LoadingState
import com.example.data.models.auth.UserAuthState
import com.example.data.models.collections.collection.AnimeCollection
import com.example.data.models.releases.anime_details.AnimeDetails
import com.example.data.models.common.request.request_parameters.Collection

/**
 * Represents the complete, immutable UI state for the Anime Details screen.
 * * This state is designed to be the "Single Source of Truth" for the view,
 * grouping related properties into nested data classes to maintain clarity
 * and reduce the complexity of the top-level object.
 *
 * @property loadingState General loading and error status for the main anime content.
 * @property anime The detailed information of the anime, or null if not yet loaded.
 * @property watchedEps A list of indices representing the episodes the user has watched.
 * @property favoritesState State related to the user's favorite list and its sync status.
 * @property collectionsState State for the user's personal collections (e.g., 'Watching', 'Dropped').
 * @property authState State for the authentication bottom sheet and login form inputs.
 * @property isDescriptionExpanded Controls the expansion toggle for the anime description text.
 */
@Immutable
data class AnimeDetailsState(
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
    val authState: AuthState = AuthState(userAuthState = UserAuthState.LoggedIn), // TODO: Crutch rewrite!!!

    // UI Toggles
    val isDescriptionExpanded: Boolean = false,
) {
    // --- Nested States ---
    @Immutable
    data class FavoritesState(
        val loadingState: LoadingState = LoadingState(),
        val ids: List<Int> = emptyList()
    )

    /**
     * Encapsulates state for the Collections bottom sheet and its data.
     * @property collectionBSVisible Visibility toggle for the collections bottom sheet.
     * @property loadingState Loading status for fetching or updating collections.
     * @property collections The list of [AnimeCollection] items associated with the user.
     */
    @Immutable
    data class CollectionsState(
        val collectionBSVisible: Boolean = false,
        val loadingState: LoadingState = LoadingState(isLoading = true),
        val collections: List<AnimeCollection> = emptyList()
    ) {
        fun toggleBS() = copy(collectionBSVisible = !collectionBSVisible)
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
