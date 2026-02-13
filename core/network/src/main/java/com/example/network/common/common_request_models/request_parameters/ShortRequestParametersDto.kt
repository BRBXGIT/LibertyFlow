package com.example.network.common.common_request_models.request_parameters

import com.google.gson.annotations.SerializedName

/**
 * A simplified version of request parameters, typically used for quick filters
 * or basic search functionality where genres and years are passed as raw strings.
 */
data class ShortRequestParametersDto(
    @field:SerializedName(FIELD_SORTING)
    override val sorting: String,
    @field:SerializedName(FIELD_AGE_RATINGS)
    override val ageRatings: List<String>,
    @field:SerializedName(FIELD_SEARCH)
    override val search: String,
    @field:SerializedName(FIELD_TYPES)
    override val types: List<String>,

    @field:SerializedName(FIELD_GENRES)
    val genres: String,
    @field:SerializedName(FIELD_YEARS)
    val years: String
): RequestParametersDtoBase {
    companion object Fields {
        private const val FIELD_SORTING = "sorting"
        private const val FIELD_AGE_RATINGS = "age_ratings"
        private const val FIELD_SEARCH = "search"
        private const val FIELD_TYPES = "types"
        private const val FIELD_GENRES = "genres"
        private const val FIELD_YEARS = "years"
    }
}