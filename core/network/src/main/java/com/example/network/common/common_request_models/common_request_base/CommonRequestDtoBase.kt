package com.example.network.common.common_request_models.common_request_base

import com.example.network.common.common_request_models.request_parameters.RequestParametersDtoBase

/**
 * Base contract for common request parameters used across various catalog and collection endpoints.
 */
interface CommonRequestDtoBase {
    val exclude: String
    val requestParameters: RequestParametersDtoBase
    val include: String
    val limit: Int
    val page: Int

    /**
     * Creates a copy of the request with updated pagination parameters.
     */
    fun withPageAndLimit(page: Int): CommonRequestDtoBase
}