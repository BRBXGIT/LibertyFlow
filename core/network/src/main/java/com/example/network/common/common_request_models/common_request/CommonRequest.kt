package com.example.network.common.common_request_models.common_request

import com.example.network.common.common_request_models.request_parameters.RequestParametersBase
import com.example.network.common.common_utils.CommonUtils
import com.google.gson.annotations.SerializedName

data class CommonRequest(
    @SerializedName("exclude") val exclude: String = CommonUtils.COMMON_EXCLUDE,
    @SerializedName("f") val requestParameters: RequestParametersBase,
    @SerializedName("include") val include: String = CommonUtils.COMMON_INCLUDE,
    @SerializedName("limit") val limit: Int = CommonUtils.COMMON_LIMIT,
    @SerializedName("page") val page: Int = CommonUtils.COMMON_START_PAGE
)