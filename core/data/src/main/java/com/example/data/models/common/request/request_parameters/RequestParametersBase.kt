package com.example.data.models.common.request.request_parameters

interface RequestParametersBase {
    val types: List<Type>
    val search: String
    val ageRatings: List<AgeRating>
    val sorting: Sorting
}