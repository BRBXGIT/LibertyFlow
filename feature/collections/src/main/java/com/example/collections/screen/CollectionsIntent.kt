package com.example.collections.screen

import com.example.data.models.common.request.request_parameters.Collection

sealed interface CollectionsIntent {
    // Auth
    object GetTokens: CollectionsIntent
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