package com.example.data.models.common.request.request_parameters

import com.example.data.models.common.common.UiGenre

data class UiFullRequestParameters(
    override val ageRatings: List<AgeRating>,
    override val genres: List<UiGenre>,
    override val search: String,
    override val types: List<Type>,

    val seasons: List<Season>,
    val years: UiYear,
    val sorting: Sorting,
    val publishStatuses: List<PublishStatus>,
    val productionStatuses: List<ProductionsStatus>
): UiRequestParametersBase(genres, types, search, ageRatings)