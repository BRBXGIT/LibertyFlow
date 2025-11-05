package com.example.data.models.common.request.request_parameters

import com.example.data.models.common.common.UiGenre

abstract class UiRequestParametersBase(
    open val genres: List<UiGenre>,
    open val types: List<Type>,
    open val search: String,
    open val ageRatings: List<AgeRating>
)