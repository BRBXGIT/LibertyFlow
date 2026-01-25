package com.example.network.common.common_request_models.common_request_base

import com.example.network.common.common_request_models.request_parameters.RequestParametersDtoBase

interface CommonRequestDtoBase {
    val exclude: String
    val requestParameters: RequestParametersDtoBase
    val include: String
    val limit: Int
    val page: Int

    fun withPageAndLimit(page: Int): CommonRequestDtoBase
}