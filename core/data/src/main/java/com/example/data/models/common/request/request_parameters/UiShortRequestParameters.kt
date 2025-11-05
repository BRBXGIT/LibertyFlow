package com.example.data.models.common.request.request_parameters

import com.example.data.models.common.common.UiGenre

data class UiShortRequestParameters(
    override val ageRatings: List<AgeRating>,
    override val genres: List<UiGenre>,
    override val search: String,
    override val types: List<Type>,

    val years: List<Int>
): UiRequestParametersBase(genres, types, search, ageRatings)
