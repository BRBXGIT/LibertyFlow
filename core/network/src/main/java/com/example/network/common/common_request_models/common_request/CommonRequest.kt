package com.example.network.common.common_request_models.common_request

import com.example.network.common.common_request_models.request_parameters.RequestParametersBase
import com.example.network.common.common_utils.CommonNetworkUtils
import com.google.gson.annotations.SerializedName

data class CommonRequest(
    @SerializedName("exclude") val exclude: String = CommonNetworkUtils.COMMON_EXCLUDE,
    @SerializedName("f") val requestParameters: RequestParametersBase,
    @SerializedName("include") val include: String = CommonNetworkUtils.COMMON_INCLUDE,
    @SerializedName("limit") val limit: Int = CommonNetworkUtils.COMMON_LIMIT,
    @SerializedName("page") val page: Int = CommonNetworkUtils.COMMON_START_PAGE
)