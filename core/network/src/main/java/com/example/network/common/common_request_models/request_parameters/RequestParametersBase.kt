package com.example.network.common.common_request_models.request_parameters

abstract class RequestParametersBase(
    open val genres: String,
    open val types: List<String>,
    open val search: String,
    open val ageRatings: List<String>,
)