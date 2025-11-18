package com.example.data.models.common.request.request_parameters

interface UiRequestParametersBase {
    val types: List<Type>
    val search: String
    val ageRatings: List<AgeRating>
    val sorting: Sorting
}