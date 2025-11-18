package com.example.data.models.common.request.common_request

import com.example.data.models.common.request.request_parameters.CollectionType
import com.example.data.models.common.request.request_parameters.UiRequestParametersBase

data class UiCommonRequestWithCollectionType(
    override val requestParameters: UiRequestParametersBase,

    val collectionType: CollectionType
): UICommonRequestBase
