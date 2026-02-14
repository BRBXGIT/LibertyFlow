package com.example.data.models.common.request.common_request

import com.example.data.models.common.request.request_parameters.Collection
import com.example.data.models.common.request.request_parameters.RequestParametersBase

/**
 * An extended implementation of [CommonRequestBase] that includes collection data.
 * * This is used for requests that need to associate [requestParameters]
 * with a specific [Collection] context.
 */
data class CommonRequestWithCollectionType(
    override val requestParameters: RequestParametersBase,

    val collection: Collection
): CommonRequestBase
