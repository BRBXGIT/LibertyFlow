package com.example.data.models.common.request.common_request

import com.example.data.models.common.request.request_parameters.Collection
import com.example.data.models.common.request.request_parameters.RequestParametersBase

data class CommonRequestWithCollectionType(
    override val requestParameters: RequestParametersBase,

    val collection: Collection
): CommonRequestBase
