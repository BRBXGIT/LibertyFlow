package com.example.network.common.common_request_models.common_request

import com.example.network.common.common_request_models.common_request_base.CommonRequestBase
import com.example.network.common.common_request_models.request_parameters.RequestParametersBase
import com.example.network.common.common_utils.CommonNetworkConstants
import com.google.gson.annotations.SerializedName

data class CommonRequest(
    @SerializedName("exclude") override val exclude: String = CommonNetworkConstants.COMMON_EXCLUDE,
    @SerializedName("f") override val requestParameters: RequestParametersBase,
    @SerializedName("include") override val include: String = CommonNetworkConstants.COMMON_INCLUDE,
    @SerializedName("limit") override val limit: Int = CommonNetworkConstants.COMMON_LIMIT,
    @SerializedName("page") override val page: Int = CommonNetworkConstants.COMMON_START_PAGE
): CommonRequestBase {
    override fun withPageAndLimit(currentPage: Int) = copy(page = currentPage + 1, limit = limit)
}