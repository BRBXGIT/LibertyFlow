package com.example.network.common.common_request_models.common_request

import com.example.network.common.common_request_models.request_parameters.RequestParametersBase

abstract class CommonRequestBase(
    open val exclude: String,
    open val requestParameters: RequestParametersBase,
    open val include: String,
    open val limit: Int,
    open val page: Int
) {
    abstract fun withPageAndLimit(): CommonRequestBase
}