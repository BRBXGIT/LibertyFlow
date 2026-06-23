package com.brbx.data.network.common

import com.brbx.domain.network.model.common.DomainGenre
import com.brbx.domain.network.model.common.DomainYears
import com.brbx.domain.network.model.response.common.DomainAnimeItem
import com.brbx.domain.network.model.response.common.DomainName
import com.brbx.domain.network.model.response.common.DomainPoster
import com.brbx.network.base.model.common.Genre
import com.brbx.network.base.model.request.Parameters
import com.brbx.network.base.model.response.common.AnimeItem
import com.brbx.network.base.model.response.common.Name
import com.brbx.network.base.model.response.common.Poster

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