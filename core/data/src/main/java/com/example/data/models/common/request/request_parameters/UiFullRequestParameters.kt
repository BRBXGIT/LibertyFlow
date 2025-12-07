package com.example.data.models.common.request.request_parameters

import com.example.data.models.common.common.UiGenre

data class UiFullRequestParameters(
    override val ageRatings: List<AgeRating> = emptyList(),
    override val sorting: Sorting = Sorting.FRESH_AT_DESC,
    override val search: String = "",
    override val types: List<Type> = emptyList(),

    val genres: List<UiGenre> = emptyList(),
    val seasons: List<Season> = emptyList(),
    val years: UiYear = UiYear(),
    val publishStatuses: List<PublishStatus> = emptyList(),
    val productionStatuses: List<ProductionsStatus> = emptyList()
): UiRequestParametersBase {
    fun updateYear(
        from: Int? = null,
        to: Int? = null
    ) = copy(
        years = years.copy(
            from = from ?: years.from,
            to = to ?: years.to
        )
    )

    fun updateSearch(search: String) = copy(search = search)

    fun addGenre(genre: UiGenre) = copy(genres = genres + genre)

    fun removeGenre(genre: UiGenre) = copy(genres = genres - genre)

    fun addSeason(season: Season) = copy(seasons = seasons + season)

    fun removeSeason(season: Season) = copy(seasons = seasons - season)

    fun updateSorting(sorting: Sorting) = copy(sorting = sorting)

    fun toggleIsOngoing(publishStatuses: List<PublishStatus>) = copy(publishStatuses = publishStatuses)
}