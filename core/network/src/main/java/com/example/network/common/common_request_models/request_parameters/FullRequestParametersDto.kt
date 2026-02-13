package com.example.network.common.common_request_models.request_parameters

import com.google.gson.annotations.SerializedName

/**
 * A comprehensive set of request parameters for advanced filtering,
 * supporting specific ID lists for genres and structured year ranges.
 */
data class FullRequestParametersDto(
    @field:SerializedName(FIELD_AGE_RATINGS)
    override val ageRatings: List<String>,
    @field:SerializedName(FIELD_SEARCH)
    override val search: String,
    @field:SerializedName(FIELD_TYPES)
    override val types: List<String>,
    @field:SerializedName(FIELD_SORTING)
    override val sorting: String,

    @field:SerializedName(FIELD_GENRES)
    val genres: List<Int>,
    @field:SerializedName(FIELD_SEASONS)
    val seasons: List<String>,
    @field:SerializedName(FIELD_YEARS)
    val yearsDto: YearsDto,
    @field:SerializedName(FIELD_PUBLISH_STATUSES)
    val publishStatuses: List<String>,
    @field:SerializedName(FIELD_PRODUCTION_STATUSES)
    val productionStatuses: List<String>
): RequestParametersDtoBase {
    companion object Fields {
        private const val FIELD_AGE_RATINGS = "age_ratings"
        private const val FIELD_SEARCH = "search"
        private const val FIELD_TYPES = "types"
        private const val FIELD_SORTING = "sorting"
        private const val FIELD_GENRES = "genres"
        private const val FIELD_SEASONS = "seasons"
        private const val FIELD_YEARS = "years"
        private const val FIELD_PUBLISH_STATUSES = "publish_statuses"
        private const val FIELD_PRODUCTION_STATUSES = "production_statuses"
    }
}
