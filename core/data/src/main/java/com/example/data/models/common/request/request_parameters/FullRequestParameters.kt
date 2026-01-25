package com.example.data.models.common.request.request_parameters

import androidx.compose.runtime.Immutable
import com.example.data.models.common.common.Genre

@Immutable
data class FullRequestParameters(
    override val ageRatings: List<AgeRating> = emptyList(),
    override val sorting: Sorting = Sorting.FRESH_AT_DESC,
    override val search: String = "",
    override val types: List<Type> = emptyList(),

    val genres: List<Genre> = emptyList(),
    val seasons: List<Season> = emptyList(),
    val years: Year = Year(),
    val publishStatuses: List<PublishStatus> = emptyList(),
    val productionStatuses: List<ProductionsStatus> = emptyList()
): RequestParametersBase {
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

    fun addGenre(genre: Genre) = copy(genres = genres + genre)

    fun removeGenre(genre: Genre) = copy(genres = genres - genre)

    fun addSeason(season: Season) = copy(seasons = seasons + season)

    fun removeSeason(season: Season) = copy(seasons = seasons - season)

    fun updateSorting(sorting: Sorting) = copy(sorting = sorting)

    fun toggleIsOngoing(publishStatuses: List<PublishStatus>) = copy(publishStatuses = publishStatuses)
}