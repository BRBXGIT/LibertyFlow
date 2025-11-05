package com.example.data.models.common.request.request_parameters

import com.example.data.models.common.common.UiGenre

data class UiFullRequestParameters(
    override val ageRatings: List<AgeRating>,
    override val sorting: Sorting,
    override val search: String,
    override val types: List<Type>,

    val genres: List<UiGenre>,
    val seasons: List<Season>,
    val years: UiYear,
    val publishStatuses: List<PublishStatus>,
    val productionStatuses: List<ProductionsStatus>
): UiRequestParametersBase(types, search, ageRatings, sorting)