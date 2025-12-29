package com.example.collections.screen

import com.example.data.models.common.request.request_parameters.Collection

sealed interface CollectionsIntent {
    object GetTokens: CollectionsIntent

    data object ToggleIsSearching: CollectionsIntent
    data object ToggleIsAuthBSVisible: CollectionsIntent

    data class UpdateQuery(val query: String): CollectionsIntent
    data class UpdateEmail(val email: String): CollectionsIntent
    data class UpdatePassword(val password: String): CollectionsIntent

    data class SetIsError(val value: Boolean): CollectionsIntent
    data class SetCollection(val collection: Collection): CollectionsIntent
}