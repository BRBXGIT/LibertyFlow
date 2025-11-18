package com.example.data.models.common.request.request_parameters

import com.example.data.models.common.common.UiGenre

data class UiFullRequestParameters(
    override val ageRatings: List<AgeRating> = emptyList(),
    override val sorting: Sorting = Sorting.RATING_DESC,
    override val search: String = "",
    override val types: List<Type> = emptyList(),

    val genres: List<UiGenre> = emptyList(),
    val seasons: List<Season> = emptyList(),
    val years: UiYear = UiYear(),
    val publishStatuses: List<PublishStatus> = emptyList(),
    val productionStatuses: List<ProductionsStatus> = emptyList()
): UiRequestParametersBase