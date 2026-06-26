package com.brbx.common.view_model.model.intent

sealed interface CommonSearchIntent {
    data object ToggleSearching : CommonSearchIntent

    @JvmInline value class UpdateSearch(val search: String) : CommonSearchIntent
}