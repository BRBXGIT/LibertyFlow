package com.example.network.common.common_request_models.common_request_base

import com.example.network.common.common_request_models.request_parameters.RequestParametersBase

interface CommonRequestBase {
    val exclude: String
    val requestParameters: RequestParametersBase
    val include: String
    val limit: Int
    val page: Int

    fun withPageAndLimit(): CommonRequestBase
}