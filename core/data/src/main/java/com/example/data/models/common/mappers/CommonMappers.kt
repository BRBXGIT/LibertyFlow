package com.example.data.models.common.mappers

import com.example.data.R
import com.example.data.models.common.common.UiGenre
import com.example.data.models.common.common.UiName
import com.example.data.models.common.common.UiPoster
import com.example.data.models.common.request.common_request.UiCommonRequest
import com.example.data.models.common.request.common_request.UiCommonRequestWithCollectionType
import com.example.data.models.common.request.request_parameters.Collection
import com.example.data.models.common.request.request_parameters.Season
import com.example.data.models.common.request.request_parameters.Sorting
import com.example.data.models.common.request.request_parameters.UiFullRequestParameters
import com.example.data.models.common.request.request_parameters.UiRequestParametersBase
import com.example.data.models.common.request.request_parameters.UiShortRequestParameters
import com.example.data.models.common.request.request_parameters.UiYear
import com.example.data.models.common.ui_anime_item.UiAnimeItem
import com.example.network.common.common_request_models.common_request.CommonRequest
import com.example.network.common.common_request_models.common_request.CommonRequestWithCollectionType
import com.example.network.common.common_request_models.request_parameters.FullRequestParameters
import com.example.network.common.common_request_models.request_parameters.RequestParametersBase
import com.example.network.common.common_request_models.request_parameters.ShortRequestParameters
import com.example.network.common.common_request_models.request_parameters.Years
import com.example.network.common.common_response_models.AnimeResponseItem
import com.example.network.common.common_response_models.Genre
import com.example.network.common.common_response_models.Name
import com.example.network.common.common_response_models.Poster

fun Season.toName(): Int {
    return when(this) {
        Season.WINTER -> R.string.winter
        Season.AUTUMN -> R.string.autumn
        Season.SPRING -> R.string.spring
        Season.SUMMER -> R.string.summer
    }
}

fun Collection.toName(): Int {
    return when(this) {
        Collection.PLANNED -> R.string.planned_collection
        Collection.WATCHED -> R.string.watched_collection
        Collection.WATCHING -> R.string.watching_collection
        Collection.POSTPONED -> R.string.postponed_collection
        Collection.ABANDONED -> R.string.abandoned_collection
    }
}

fun Collection.toIndex(): Int {
    return when(this) {
        Collection.PLANNED -> 0
        Collection.WATCHED -> 1
        Collection.WATCHING -> 2
        Collection.POSTPONED -> 3
        Collection.ABANDONED -> 4
    }
}

fun Collection.toPage(): Int {
    return when(this) {
        Collection.PLANNED -> 1
        Collection.WATCHED -> 2
        Collection.WATCHING -> 3
        Collection.POSTPONED -> 4
        Collection.ABANDONED -> 5
    }
}

fun Int.toCollection(): Collection {
    return when(this) {
        0 -> Collection.PLANNED
        1 -> Collection.WATCHED
        2 -> Collection.WATCHING
        3 -> Collection.POSTPONED
        else -> Collection.ABANDONED
    }
}

fun Sorting.toName(): Int {
    return when(this) {
        Sorting.RATING_DESC -> R.string.by_popularity_label
        else -> R.string.by_novelty_label
    }
}

internal fun AnimeResponseItem.toUiAnimeItem(): UiAnimeItem {
    return UiAnimeItem(
        id = id,
        genres = genres.map { it.toUiGenre() },
        poster = poster.toUiPoster(),
        name = name.toUiName()
    )
}

internal fun Genre.toUiGenre(): UiGenre {
    return UiGenre(
        id = id,
        name = name
    )
}

internal fun Poster.toUiPoster(): UiPoster {
    return UiPoster(
        thumbnail = thumbnail,
        preview = preview,
        src = src
    )
}

internal fun Name.toUiName(): UiName {
    return UiName(
        russian = main,
        english = english,
        alternative = alternative
    )
}

internal fun UiCommonRequest.toCommonRequest(): CommonRequest {
    return CommonRequest(requestParameters = requestParameters.toRequestParametersBase())
}

internal fun UiCommonRequestWithCollectionType.toCommonRequestWithCollectionType(): CommonRequestWithCollectionType {
    return CommonRequestWithCollectionType(
        requestParameters = requestParameters.toRequestParametersBase(),
        collectionType = collection.name
    )
}

internal fun UiRequestParametersBase.toRequestParametersBase(): RequestParametersBase {
    return when (this) {
        is UiShortRequestParameters -> this.toShortRequestParameters()
        is UiFullRequestParameters -> this.toFullRequestParameters()
        else -> throw IllegalArgumentException("Unsupported UiRequestParametersBase type: ${this::class}")
    }
}

internal fun UiShortRequestParameters.toShortRequestParameters(): ShortRequestParameters {
    return ShortRequestParameters(
        ageRatings = ageRatings.map { it.name },
        genres = genres.joinToString(", ") { it.id.toString() },
        search = search,
        types = types.map { it.name },
        sorting = sorting.name,
        years = years.joinToString(", ") { it.toString() }
    )
}

internal fun UiFullRequestParameters.toFullRequestParameters(): FullRequestParameters {
    return FullRequestParameters(
        ageRatings = ageRatings.map { it.name },
        genres = genres.map { it.id },
        search = search,
        types = types.map { it.name },
        seasons = seasons.map { it.name.lowercase() },
        years = years.toYears(),
        sorting = sorting.name,
        publishStatuses = publishStatuses.map { it.name },
        productionStatuses = productionStatuses.map { it.name }
    )
}

internal fun UiYear.toYears(): Years {
    return Years(
        fromYear = from,
        toYear = to
    )
}