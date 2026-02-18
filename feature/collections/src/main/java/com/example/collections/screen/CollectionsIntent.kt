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

    /**
     * Intent to update specific fields within the authentication form.
     * * This is grouped into a nested structure to prevent the root [CollectionsIntent]
     * from becoming cluttered with individual field update classes.
     * @property field The specific [AuthField] being modified.
     */
    data class UpdateAuthForm(val field: AuthField): CollectionsIntent { // Grouped form updates to avoid polluting the intent root
        sealed interface AuthField {
            data class Email(val value: String): AuthField
            data class Password(val value: String): AuthField
        }
    }

    // Toggles
    data object ToggleIsSearching: CollectionsIntent
    data object ToggleIsAuthBSVisible: CollectionsIntent

    // Sets
    data class SetIsError(val value: Boolean): CollectionsIntent
    data class SetCollection(val collection: Collection): CollectionsIntent

    // Updates
    data class UpdateQuery(val query: String): CollectionsIntent
}