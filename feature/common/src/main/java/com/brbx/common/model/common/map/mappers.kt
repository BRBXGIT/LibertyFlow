package com.brbx.common.model.common.map

import com.brbx.common.common.CommonConstants
import com.brbx.common.model.common.model.Genre
import com.brbx.common.model.common.model.Name
import com.brbx.common.model.common.model.Poster
import com.brbx.common.model.common.model.Years
import com.brbx.design_system.component.anime_card.AnimeCardModel
import com.brbx.domain.network.model.common.DomainGenre
import com.brbx.domain.network.model.common.DomainYears
import com.brbx.domain.network.model.response.common.DomainAnimeItem
import com.brbx.domain.network.model.response.common.DomainName
import com.brbx.domain.network.model.response.common.DomainPoster
import com.brbx.ui_compose.common.toBrbxText

fun DomainGenre.toAnimeCardModel(): Genre =
    Genre(
        id = this.id,
        name = this.name,
    )

fun DomainName.toAnimeCardModel(): Name =
    Name(
        alternative = this.alternative,
        english = this.english,
        russian = this.main,
    )

fun DomainPoster.toAnimeCardModel(): Poster =
    Poster(
        preview = this.preview,
        src = this.src,
        thumbnail = this.thumbnail,
    )

fun DomainYears.toAnimeCardModel(): Years =
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

fun DomainAnimeItem.toAnimeCardModel(): AnimeCardModel =
    AnimeCardModel(
        genres = this.genres.map { genre -> genre.name },
        id = this.id,
        name = this.name.main.toBrbxText(),
        fullPosterPath = CommonConstants.BasePosterPath + this.poster.preview,
    )