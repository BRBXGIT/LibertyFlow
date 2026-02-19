package com.example.collections.screen

import com.example.data.models.common.request.request_parameters.Collection

/**
 * Sealed interface representing all possible user or system actions (Intents)
 * for the Collections screen.
 * * This interface follows the Unidirectional Data Flow (UDF) pattern,
 * acting as the single entry point for all events dispatched to the [CollectionsVM].
 */
sealed interface CollectionsIntent {
    // Auth
    object GetTokens: CollectionsIntent
    data class UpdateLogin(val login: String): CollectionsIntent
    data class UpdatePassword(val password: String): CollectionsIntent


    // Toggles
    data object ToggleIsSearching: CollectionsIntent
    data object ToggleIsAuthBSVisible: CollectionsIntent


    // Sets
    data class SetCollection(val collection: Collection): CollectionsIntent


    // Updates
    data class UpdateQuery(val query: String): CollectionsIntent
}