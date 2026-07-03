package com.brbx.common.view_model.processor.search.model

sealed interface CommonSearchIntent {

    data object ToggleSearching : CommonSearchIntent

    @JvmInline value class UpdateSearch(val search: String) : CommonSearchIntent
}