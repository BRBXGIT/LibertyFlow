package com.example.network.common.common_request_models.request_parameters

interface RequestParametersDtoBase {
    val types: List<String>
    val search: String
    val ageRatings: List<String>
    val sorting: String
}