package com.example.network.common.common_request_models.request_parameters

import com.google.gson.annotations.SerializedName

data class FullRequestParametersDto(
    @SerializedName("age_ratings") override val ageRatings: List<String>,
    @SerializedName("search") override val search: String,
    @SerializedName("types") override val types: List<String>,
    @SerializedName("sorting") override val sorting: String,

    @SerializedName("genres") val genres: List<Int>,
    @SerializedName("seasons") val seasons: List<String>,
    @SerializedName("years") val yearsDto: YearsDto,
    @SerializedName("publish_statuses") val publishStatuses: List<String>,
    @SerializedName("production_statuses") val productionStatuses: List<String>
): RequestParametersDtoBase
