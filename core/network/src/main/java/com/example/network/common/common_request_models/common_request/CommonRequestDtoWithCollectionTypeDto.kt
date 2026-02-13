package com.example.network.common.common_request_models.common_request

import com.example.network.common.common_request_models.common_request_base.CommonRequestDtoBase
import com.example.network.common.common_request_models.request_parameters.RequestParametersDtoBase
import com.example.network.common.common_utils.CommonNetworkConstants
import com.google.gson.annotations.SerializedName

/**
 * Extended common request DTO that includes a specific collection filter.
 */
data class CommonRequestDtoWithCollectionTypeDto(
    @field:SerializedName(FIELD_EXCLUDE)
    override val exclude: String = CommonNetworkConstants.COMMON_EXCLUDE,

    @field:SerializedName(FIELD_PARAMETERS)
    override val requestParameters: RequestParametersDtoBase,

    @field:SerializedName(FIELD_INCLUDE)
    override val include: String = CommonNetworkConstants.COMMON_INCLUDE,

    @field:SerializedName(FIELD_LIMIT)
    override val limit: Int = CommonNetworkConstants.COMMON_LIMIT,

    @field:SerializedName(FIELD_PAGE)
    override val page: Int = CommonNetworkConstants.COMMON_START_PAGE,

    @field:SerializedName(FIELD_COLLECTION_TYPE)
    val collectionType: String
): CommonRequestDtoBase {

    override fun withPageAndLimit(page: Int) = copy(page = page)

    companion object Fields {
        private const val FIELD_EXCLUDE = "exclude"
        private const val FIELD_PARAMETERS = "f"
        private const val FIELD_INCLUDE = "include"
        private const val FIELD_LIMIT = "limit"
        private const val FIELD_PAGE = "page"
        private const val FIELD_COLLECTION_TYPE = "type_of_collection"
    }
}
