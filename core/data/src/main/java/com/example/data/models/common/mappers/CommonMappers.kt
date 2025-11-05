package com.example.data.models.common.mappers

import com.example.data.models.common.common.UiGenre
import com.example.data.models.common.common.UiName
import com.example.data.models.common.common.UiPoster
import com.example.data.models.common.request.common_request.UiCommonRequest
import com.example.data.models.common.request.request_parameters.UiFullRequestParameters
import com.example.data.models.common.request.request_parameters.UiRequestParametersBase
import com.example.data.models.common.request.request_parameters.UiShortRequestParameters
import com.example.data.models.common.request.request_parameters.UiYear
import com.example.data.models.common.ui_anime_item.UiAnimeItem
import com.example.network.common.common_request_models.common_request.CommonRequest
import com.example.network.common.common_request_models.request_parameters.FullRequestParameters
import com.example.network.common.common_request_models.request_parameters.RequestParametersBase
import com.example.network.common.common_request_models.request_parameters.ShortRequestParameters
import com.example.network.common.common_request_models.request_parameters.Years
import com.example.network.common.common_response_models.AnimeResponseItem
import com.example.network.common.common_response_models.Genre
import com.example.network.common.common_response_models.Name
import com.example.network.common.common_response_models.Poster

fun AnimeResponseItem.toUiAnimeItem(): UiAnimeItem {
    return UiAnimeItem(
        id = id,
        genres = genres.map { it.toUiGenre() },
        poster = poster.toUiPoster(),
        name = name.toUiName()
    )
}

fun Genre.toUiGenre(): UiGenre {
    return UiGenre(
        id = id,
        name = name
    )
}

fun Poster.toUiPoster(): UiPoster {
    return UiPoster(
        thumbnail = thumbnail,
        preview = preview,
        src = src
    )
}

fun Name.toUiName(): UiName {
    return UiName(
        russian = main,
        english = english,
        alternative = alternative
    )
}

fun UiCommonRequest.toCommonRequest(): CommonRequest {
    return CommonRequest(requestParameters = requestParameters.toRequestParametersBase())
}

fun UiRequestParametersBase.toRequestParametersBase(): RequestParametersBase {
    return when (this) {
        is UiShortRequestParameters -> this.toShortRequestParameters()
        is UiFullRequestParameters -> this.toFullRequestParameters()
        else -> throw IllegalArgumentException("Unsupported UiRequestParametersBase type: ${this::class}")
    }
}

fun UiShortRequestParameters.toShortRequestParameters(): ShortRequestParameters {
    return ShortRequestParameters(
        ageRatings = ageRatings.map { it.name },
        genres = genres.joinToString(", ") { it.id.toString() },
        search = search,
        types = types.map { it.name },
        sorting = sorting.name,
        years = years.joinToString(", ") { it.toString() }
    )
}

fun UiFullRequestParameters.toFullRequestParameters(): FullRequestParameters {
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

fun UiYear.toYears(): Years {
    return Years(
        fromYear = from,
        toYear = to
    )
}