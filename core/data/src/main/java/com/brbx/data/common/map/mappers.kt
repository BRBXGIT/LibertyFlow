package com.brbx.data.common.map

import com.brbx.domain.model.common.DomainGenre
import com.brbx.domain.model.common.PublishStatus
import com.brbx.domain.model.common.Season
import com.brbx.domain.model.common.Type
import com.brbx.domain.model.response.common.DomainAnimeItem
import com.brbx.domain.model.response.common.DomainName
import com.brbx.domain.model.response.common.DomainPoster
import com.brbx.domain.model.result.DomainRequestException
import com.brbx.domain.model.result.DomainRequestResult
import com.brbx.network.base.model.common.Genre
import com.brbx.network.base.model.response.common.AnimeItem
import com.brbx.network.base.model.response.common.Name
import com.brbx.network.base.model.response.common.Poster
import com.brbx.network.base.model.result.RequestException
import com.brbx.network.base.model.result.RequestResult

private fun RequestException.toDomain(): DomainRequestException =
    when (this) {
        RequestException.CONFLICT -> DomainRequestException.CONFLICT
        RequestException.TOO_MANY_REQUESTS -> DomainRequestException.TOO_MANY_REQUESTS
        RequestException.PAYLOAD_TOO_LARGE -> DomainRequestException.PAYLOAD_TOO_LARGE
        RequestException.SERVER_ERROR -> DomainRequestException.SERVER_ERROR
        RequestException.INCORRECT_CREDENTIALS -> DomainRequestException.INCORRECT_CREDENTIALS
        RequestException.UNAUTHORIZED -> DomainRequestException.UNAUTHORIZED
        RequestException.NO_EMAIL_OR_PASSWORD -> DomainRequestException.NO_EMAIL_OR_PASSWORD
        RequestException.REQUEST_TIMEOUT -> DomainRequestException.REQUEST_TIMEOUT
        RequestException.INTERNET -> DomainRequestException.INTERNET
        RequestException.SERIALIZATION -> DomainRequestException.SERIALIZATION
        RequestException.UNKNOWN -> DomainRequestException.UNKNOWN
    }

internal inline fun <T, R> RequestResult<T>.toDomain(
    transform: (T) -> R
): DomainRequestResult<R> =
    when (this) {
        is RequestResult.Success -> DomainRequestResult.Success(data = transform(this.data))
        is RequestResult.Error -> DomainRequestResult.Error(exception = this.exception.toDomain())
    }

internal fun AnimeItem.toDomain(): DomainAnimeItem =
    DomainAnimeItem(
        favoritesCount = this.addedInUsersFavorites,
        genres = this.genres.map { it.toDomain() },
        id = this.id,
        name = this.name.toDomain(),
        poster = this.poster.toDomain(),
    )

internal fun Genre.toDomain(): DomainGenre =
    DomainGenre(
        id = this.id,
        name = this.name,
    )

internal fun Name.toDomain(): DomainName =
    DomainName(
        alternative = this.alternative,
        english = this.english,
        main = this.main,
    )

internal fun Poster.toDomain(): DomainPoster =
    DomainPoster(
        preview = this.optimized.preview,
        src = this.optimized.src,
        thumbnail = this.optimized.thumbnail,
    )

internal fun String.toSeason(): Season =
    when (this) {
        "winter" -> Season.Winter
        "spring" -> Season.Spring
        "summer" -> Season.Summer
        "autumn" -> Season.Fall
        else -> Season.Unknown
    }

internal fun String.toType(): Type =
    when (this) {
        "TV" -> Type.TV
        "ONA" -> Type.ONA
        "WEB" -> Type.Web
        "OVA" -> Type.OVA
        "OAD" -> Type.OAD
        "MOVIE" -> Type.Movie
        "DORAMA" -> Type.Dorama
        "SPECIAL" -> Type.Special
        else -> Type.Unknown
    }

internal fun Boolean.toPublishStatus(): PublishStatus =
    when (this) {
        true -> PublishStatus.Ongoing
        false -> PublishStatus.Finished
    }