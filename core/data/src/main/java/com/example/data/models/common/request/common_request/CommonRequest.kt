package com.example.data.models.common.request.common_request

import com.example.data.models.common.request.request_parameters.RequestParametersBase

/**
 * A standard implementation of [CommonRequestBase] for simple queries.
 * * Use this for requests that only require basic parameters without
 * additional body data.
 */
data class CommonRequest(
    override val requestParameters: RequestParametersBase
): CommonRequestBase