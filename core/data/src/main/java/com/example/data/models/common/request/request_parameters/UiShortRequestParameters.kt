package com.example.data.models.common.request.request_parameters

import com.example.data.models.common.common.UiGenre

data class UiShortRequestParameters(
    override val ageRatings: List<AgeRating>,
    override val search: String,
    override val types: List<Type>,
    override val sorting: Sorting,

    val genres: List<UiGenre>,
    val years: List<Int>
): UiRequestParametersBase(types, search, ageRatings, sorting)
