package com.example.network.common.common_request_models.request_parameters

/**
 * Base interface defining the fundamental filtering and sorting criteria
 * for anime discovery requests.
 */
interface RequestParametersDtoBase {
    val types: List<String>
    val search: String
    val ageRatings: List<String>
    val sorting: String
}