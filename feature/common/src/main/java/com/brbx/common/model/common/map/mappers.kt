package com.brbx.common.model.common.map

import com.brbx.common.model.common.model.AnimeItem
import com.brbx.common.model.common.model.Genre
import com.brbx.common.model.common.model.Name
import com.brbx.common.model.common.model.Poster
import com.brbx.common.model.common.model.Years
import com.brbx.domain.network.model.common.DomainCollection
import com.brbx.domain.network.model.common.DomainGenre
import com.brbx.domain.network.model.common.DomainYears
import com.brbx.domain.network.model.response.common.DomainAnimeItem
import com.brbx.domain.network.model.response.common.DomainName
import com.brbx.domain.network.model.response.common.DomainPoster
import com.brbx.domain.network.model.result.RequestException
import com.brbx.domain.network.paging.model.PagingException
import com.brbx.feature.common.R
import com.brbx.ui_compose.common.BrbxText
import com.brbx.ui_compose.common.toBrbxText

fun DomainGenre.toUi(): Genre =
    Genre(
        id = this.id,
        name = this.name,
    )

fun DomainName.toUi(): Name =
    Name(
        alternative = this.alternative,
        english = this.english,
        russian = this.main,
    )

fun DomainPoster.toUi(): Poster =
    Poster(
        preview = this.preview,
        src = this.src,
        thumbnail = this.thumbnail,
    )

fun DomainYears.toUi(): Years =
    Years(
        from = this.fromYear,
        to = this.toYear,
    )

fun Genre.toDomain(): DomainGenre =
    DomainGenre(
        id = this.id,
        name = this.name,
    )

fun Years.toDomain(): DomainYears =
    DomainYears(
        fromYear = this.from,
        toYear = this.to,
    )

fun DomainAnimeItem.toUi(): AnimeItem =
    AnimeItem(
        genres = this.genres.map { it.toUi() },
        id = this.id,
        name = this.name.toUi(),
        posterPath = this.poster.toUi(),
    )

fun DomainCollection.toBrbxText(): BrbxText =
    when (this) {
        DomainCollection.Planned -> R.string.collection_planned
        DomainCollection.Watched -> R.string.collection_watched
        DomainCollection.Watching -> R.string.collection_watching
        DomainCollection.Postponed -> R.string.collection_postponed
        DomainCollection.Abandoned -> R.string.collection_abandoned
    }.toBrbxText()

fun RequestException.toBrbxText(): BrbxText =
    when (this) {
        RequestException.Conflict -> R.string.request_exception_conflict
        RequestException.TooManyRequests -> R.string.request_exception_many_requests
        RequestException.PayloadTooLarge -> R.string.request_exception_payload_large
        RequestException.ServerError -> R.string.request_exception_server
        RequestException.RequestTimeout -> R.string.request_exception_timeout
        RequestException.Internet -> R.string.request_exception_internet
        RequestException.Unknown -> R.string.request_exception_unknown
        RequestException.IncorrectCredentials -> R.string.request_exception_credentials
        RequestException.Unauthorized -> R.string.request_exception_unauthorized
        RequestException.NoEmailOrPassword -> R.string.request_exception_no_email_or_pwassword
    }.toBrbxText()

fun PagingException.toBrbxText(): BrbxText =
    when (this) {
        is PagingException.Conflict -> R.string.request_exception_conflict
        is PagingException.TooManyRequests -> R.string.request_exception_many_requests
        is PagingException.PayloadTooLarge -> R.string.request_exception_payload_large
        is PagingException.ServerError -> R.string.request_exception_server
        is PagingException.RequestTimeout -> R.string.request_exception_timeout
        is PagingException.Internet -> R.string.request_exception_internet
        is PagingException.Unknown -> R.string.request_exception_unknown
        is PagingException.IncorrectCredentials -> R.string.request_exception_credentials
        is PagingException.Unauthorized -> R.string.request_exception_unauthorized
        is PagingException.NoEmailOrPassword -> R.string.request_exception_no_email_or_pwassword
    }.toBrbxText()