package com.example.data.models.common.request.request_parameters

abstract class UiRequestParametersBase(
    open val types: List<Type>,
    open val search: String,
    open val ageRatings: List<AgeRating>,
    open val sorting: Sorting
)