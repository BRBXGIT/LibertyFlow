package com.brbx.common.model.common.map

import com.brbx.common.model.common.model.Genre
import com.brbx.common.model.common.model.Name
import com.brbx.common.model.common.model.Poster
import com.brbx.common.model.common.model.Years
import com.brbx.domain.network.model.common.DomainGenre
import com.brbx.domain.network.model.common.DomainYears
import com.brbx.domain.network.model.response.common.DomainName
import com.brbx.domain.network.model.response.common.DomainPoster

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