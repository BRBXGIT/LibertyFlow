package com.example.network.common.common_request_models.common_request

import com.example.network.common.common_request_models.common_request_base.CommonRequestBase
import com.example.network.common.common_request_models.request_parameters.RequestParametersBase
import com.example.network.common.common_utils.CommonNetworkConstants
import com.google.gson.annotations.SerializedName

data class CommonRequestWithCollectionType(
    @SerializedName("exclude") override val exclude: String = CommonNetworkConstants.COMMON_EXCLUDE,
    @SerializedName("f") override val requestParameters: RequestParametersBase,
    @SerializedName("include") override val include: String = CommonNetworkConstants.COMMON_INCLUDE,
    @SerializedName("limit") override val limit: Int = CommonNetworkConstants.COMMON_LIMIT,
    @SerializedName("page") override val page: Int = CommonNetworkConstants.COMMON_START_PAGE,

    @SerializedName("type_of_collection") val collectionType: String
): CommonRequestBase {
    override fun withPageAndLimit() = copy(page = page, limit = limit)
}
