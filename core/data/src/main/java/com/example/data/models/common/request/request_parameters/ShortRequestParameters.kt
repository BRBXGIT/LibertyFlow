package com.example.data.models.common.request.request_parameters

import com.example.data.models.common.common.Genre

/**
 * A simplified set of parameters, likely used for quick-search or
 * side-bar filtering.
 */
data class ShortRequestParameters(
    override val ageRatings: List<AgeRating> = emptyList(),
    override val search: String = "",
    override val types: List<Type> = emptyList(),
    override val sorting: Sorting = Sorting.FRESH_AT_DESC,
    val genres: List<Genre> = emptyList(),
    val years: List<Int> = emptyList()
): RequestParametersBase
