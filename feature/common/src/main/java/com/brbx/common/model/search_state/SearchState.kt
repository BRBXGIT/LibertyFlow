package com.brbx.common.model.search_state

import androidx.compose.runtime.Immutable
import arrow.optics.optics

@Immutable
@optics
data class SearchState(
    val isSearching: Boolean = false,
    val search: String = "",
) { companion object }
