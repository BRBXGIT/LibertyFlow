package com.brbx.common.view_model.processor.search.model

import androidx.compose.runtime.Immutable
import arrow.optics.optics

@Immutable
@optics
data class CommonSearchState(
    val isSearching: Boolean = false,
    val search: String = "",
) { companion object }