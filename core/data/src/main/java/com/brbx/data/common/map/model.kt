package com.brbx.data.common.map

import com.brbx.domain.model.common.DomainGenre
import com.brbx.domain.model.common.DomainYears
import com.brbx.domain.model.response.common.DomainAnimeItem
import com.brbx.domain.model.response.common.DomainName
import com.brbx.domain.model.response.common.DomainPoster
import com.brbx.domain.model.result.DomainRequestException
import com.brbx.domain.model.result.DomainRequestResult
import com.brbx.network.base.model.common.Genre
import com.brbx.network.base.model.request.Parameters
import com.brbx.network.base.model.response.common.AnimeItem
import com.brbx.network.base.model.response.common.Name
import com.brbx.network.base.model.response.common.Poster
import com.brbx.network.base.model.result.RequestException
import com.brbx.network.base.model.result.RequestResult

private fun RequestException.toDomain(): DomainRequestException =
    when (this) {
        RequestException.Conflict -> DomainRequestException.Conflict
        RequestException.TooManyRequests -> DomainRequestException.TooManyRequests
        RequestException.PayloadTooLarge -> DomainRequestException.PayloadTooLarge
        RequestException.ServerError -> DomainRequestException.ServerError
        RequestException.IncorrectCredentials -> DomainRequestException.IncorrectCredentials
        RequestException.Unauthorized -> DomainRequestException.Unauthorized
        RequestException.NoEmailOrPassword -> DomainRequestException.NoEmailOrPassword
        RequestException.RequestTimeout -> DomainRequestException.RequestTimeout
        RequestException.Internet -> DomainRequestException.Internet
        RequestException.Serialization -> DomainRequestException.Serialization
        RequestException.Unknown -> DomainRequestException.Unknown
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

internal fun DomainYears.toData(): Parameters.Complex.Years =
    Parameters.Complex.Years(
        fromYear = this.fromYear,
        toYear = this.toYear,
    )