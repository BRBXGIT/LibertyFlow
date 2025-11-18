package com.example.network.common.common_request_models.request_parameters

import com.google.gson.annotations.SerializedName

data class ShortRequestParameters(
    @SerializedName("sorting") override val sorting: String,
    @SerializedName("age_ratings") override val ageRatings: List<String>,
    @SerializedName("search") override val search: String,
    @SerializedName("types") override val types: List<String>,

    @SerializedName("genres") val genres: String,
    @SerializedName("years") val years: String
): RequestParametersBase