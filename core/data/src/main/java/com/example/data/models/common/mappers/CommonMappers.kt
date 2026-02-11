package com.example.data.models.common.mappers

import com.example.data.R
import com.example.data.models.common.common.Genre
import com.example.data.models.common.common.Name
import com.example.data.models.common.common.Poster
import com.example.data.models.common.request.common_request.CommonRequest
import com.example.data.models.common.request.common_request.CommonRequestWithCollectionType
import com.example.data.models.common.request.request_parameters.Collection
import com.example.data.models.common.request.request_parameters.Season
import com.example.data.models.common.request.request_parameters.Sorting
import com.example.data.models.common.request.request_parameters.FullRequestParameters
import com.example.data.models.common.request.request_parameters.RequestParametersBase
import com.example.data.models.common.request.request_parameters.ShortRequestParameters
import com.example.data.models.common.request.request_parameters.Year
import com.example.data.models.common.ui_anime_item.AnimeItem
import com.example.network.common.common_request_models.common_request.CommonRequestDto
import com.example.network.common.common_request_models.common_request.CommonRequestDtoWithCollectionTypeDto
import com.example.network.common.common_request_models.request_parameters.FullRequestParametersDto
import com.example.network.common.common_request_models.request_parameters.RequestParametersDtoBase
import com.example.network.common.common_request_models.request_parameters.ShortRequestParametersDto
import com.example.network.common.common_request_models.request_parameters.YearsDto
import com.example.network.common.common_response_models.AnimeResponseItemDto
import com.example.network.common.common_response_models.GenreDto
import com.example.network.common.common_response_models.NameDto
import com.example.network.common.common_response_models.PosterDto

fun Season.toLabelRes(): Int {
    return when(this) {
        Season.WINTER -> R.string.winter
        Season.AUTUMN -> R.string.autumn
        Season.SPRING -> R.string.spring
        Season.SUMMER -> R.string.summer
    }
}

fun Collection.toLabelRes(): Int {
    return when(this) {
        Collection.PLANNED -> R.string.planned_collection
        Collection.WATCHED -> R.string.watched_collection
        Collection.WATCHING -> R.string.watching_collection
        Collection.POSTPONED -> R.string.postponed_collection
        Collection.ABANDONED -> R.string.abandoned_collection
    }
}

fun Sorting.toLabelRes(): Int {
    return when(this) {
        Sorting.RATING_DESC -> R.string.by_popularity_label
        else -> R.string.by_novelty_label
    }
}

internal fun AnimeResponseItemDto.toAnimeItem(): AnimeItem {
    return AnimeItem(
        id = id,
        genres = genres.map { it.toGenre() },
        poster = posterDto.toPoster(),
        name = nameDto.toLabelRes()
    )
}

internal fun GenreDto.toGenre(): Genre {
    return Genre(
        id = id,
        name = name
    )
}

internal fun PosterDto.toPoster(): Poster {
    return Poster(
        thumbnail = optimizedDto.thumbnail,
        preview = optimizedDto.preview,
        src = optimizedDto.src
    )
}

internal fun NameDto.toLabelRes(): Name {
    return Name(
        russian = main,
        english = english,
        alternative = alternative
    )
}

internal fun CommonRequest.toCommonRequestDto(): CommonRequestDto {
    return CommonRequestDto(requestParameters = requestParameters.toRequestParametersDtoBase())
}

internal fun CommonRequestWithCollectionType.toCommonRequestWithCollectionTypeDto(): CommonRequestDtoWithCollectionTypeDto {
    return CommonRequestDtoWithCollectionTypeDto(
        requestParameters = requestParameters.toRequestParametersDtoBase(),
        collectionType = collection.name
    )
}

internal fun RequestParametersBase.toRequestParametersDtoBase(): RequestParametersDtoBase {
    return when (this) {
        is ShortRequestParameters -> this.toShortRequestParametersDto()
        is FullRequestParameters -> this.toFullRequestParametersDto()
        else -> throw IllegalArgumentException("Unsupported UiRequestParametersBase type: ${this::class}")
    }
}

internal fun ShortRequestParameters.toShortRequestParametersDto(): ShortRequestParametersDto {
    return ShortRequestParametersDto(
        ageRatings = ageRatings.map { it.name },
        genres = genres.joinToString(", ") { it.id.toString() },
        search = search,
        types = types.map { it.name },
        sorting = sorting.name,
        years = years.joinToString(", ") { it.toString() }
    )
}

internal fun FullRequestParameters.toFullRequestParametersDto(): FullRequestParametersDto {
    return FullRequestParametersDto(
        ageRatings = ageRatings.map { it.name },
        genres = genres.map { it.id },
        search = search,
        types = types.map { it.name },
        seasons = seasons.map { it.name.lowercase() },
        yearsDto = years.toYearsDto(),
        sorting = sorting.name,
        publishStatuses = publishStatuses.map { it.name },
        productionStatuses = productionStatuses.map { it.name }
    )
}

internal fun Year.toYearsDto(): YearsDto {
    return YearsDto(
        fromYear = from,
        toYear = to
    )
}