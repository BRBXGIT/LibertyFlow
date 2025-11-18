package com.example.data.models.common.request.request_parameters

import com.example.data.models.common.common.UiGenre

data class UiShortRequestParameters(
    override val ageRatings: List<AgeRating> = emptyList(),
    override val search: String = "",
    override val types: List<Type> = emptyList(),
    override val sorting: Sorting = Sorting.RATING_DESC,

    val genres: List<UiGenre> = emptyList(),
    val years: List<Int> = emptyList()
): UiRequestParametersBase
