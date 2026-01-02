package com.example.anime_details.screen

import androidx.compose.runtime.Immutable
import com.example.data.models.auth.AuthState
import com.example.data.models.releases.anime_details.UiAnimeDetails

@Immutable
data class AnimeDetailsState(
    // Auth
    val authState: AuthState = AuthState.LoggedOut,

    // Sets
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val watchedEps: List<Int> = emptyList(),
    val isFavoritesLoading: Boolean = false,
    val isFavoritesError: Boolean = false,

    // Auth bs
    val isAuthBSVisible: Boolean = false,
    val email: String = "",
    val password: String = "",
    val isPasswordOrEmailIncorrect: Boolean = false,

    // Anime data
    val anime: UiAnimeDetails? = null,

    // Toggles
    val isDescriptionExpanded: Boolean = false,

    // Updates
    val favoritesIds: List<Int> = emptyList()
) {
    // Sets
    fun setAuthState(value: AuthState) = copy(authState = value)
    fun setWatchedEps(value: List<Int>) = copy(watchedEps = value)
    fun setIsPasswordOrEmailIncorrect(value: Boolean) = copy(isPasswordOrEmailIncorrect = value)

    // Data
    fun onDataQueryStart() = copy(isLoading = true, isError = false)
    fun onDataQueryEnd() = copy(isLoading = false)
    fun onDataQueryError() = copy(isError = true)

    // Favorites
    fun onFavoritesQueryStart() = copy(isFavoritesLoading = true, isFavoritesError = false)
    fun onFavoritesQueryEnd() = copy(isFavoritesLoading = false)
    fun onFavoritesQueryError() = copy(isFavoritesError = true)
    fun addAnimeToFavorites() = copy(favoritesIds = favoritesIds + anime!!.id)
    fun removeAnimeFromFavorites() = copy(favoritesIds = favoritesIds - anime!!.id)

    // Toggles
    fun toggleIsDescriptionExpanded() = copy(isDescriptionExpanded = !isDescriptionExpanded)
    fun toggleAuthBSVisible() = copy(isAuthBSVisible = !isAuthBSVisible)

    // Updates
    fun updateEmail(email: String) = copy(email = email)
    fun updatePassword(password: String) = copy(password = password)
}
