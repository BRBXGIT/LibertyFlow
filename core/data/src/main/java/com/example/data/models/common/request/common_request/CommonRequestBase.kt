package com.example.data.models.common.request.common_request

import com.example.data.models.common.request.request_parameters.RequestParametersBase

/**
 * A base contract for all network requests which need parameters (e.g. query, genres).
 * * This interface ensures that every request provides a [RequestParametersBase]
 * object.
 */
interface CommonRequestBase {
    val requestParameters: RequestParametersBase
}