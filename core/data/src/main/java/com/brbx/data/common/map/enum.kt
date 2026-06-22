package com.brbx.data.common.map

import com.brbx.domain.model.common.AgeRating
import com.brbx.domain.model.common.Collection
import com.brbx.domain.model.common.ProductionStatus
import com.brbx.domain.model.common.PublishStatus
import com.brbx.domain.model.common.Season
import com.brbx.domain.model.common.Sorting
import com.brbx.domain.model.common.Type

internal fun Sorting.toData(): String =
    when (this) {
        Sorting.CreatedAtDesc -> "CREATED_AT_DESC"
        Sorting.CreatedAtAsc -> "CREATED_AT_ASC"
        Sorting.FreshAtDesc -> "FRESH_AT_DESC"
        Sorting.FreshAtAsc -> "FRESH_AT_ASC"
        Sorting.RatingDesc -> "RATING_DESC"
        Sorting.RatingAsc -> "RATING_ASC"
        Sorting.YearDesc -> "YEAR_DESC"
        Sorting.YearAsc -> "YEAR_ASC"
    }

internal fun ProductionStatus.toData(): String =
    when (this) {
        ProductionStatus.InProduction -> "IS_IN_PRODUCTION"
        ProductionStatus.Finished -> "IS_NOT_IN_PRODUCTION"
    }

internal fun String.toSeason(): Season =
    when (this) {
        "winter" -> Season.Winter
        "spring" -> Season.Spring
        "summer" -> Season.Summer
        "autumn" -> Season.Fall
        else -> Season.Unknown
    }

internal fun Season.toData(): String =
    when (this) {
        Season.Winter -> "winter"
        Season.Spring -> "spring"
        Season.Summer -> "summer"
        Season.Fall -> "autumn"
        Season.Unknown -> ""
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

internal fun Type.toData(): String =
    when (this) {
        Type.TV -> "TV"
        Type.ONA -> "ONA"
        Type.Web -> "WEB"
        Type.OVA -> "OVA"
        Type.OAD -> "OAD"
        Type.Movie -> "MOVIE"
        Type.Dorama -> "DORAMA"
        Type.Special -> "SPECIAL"
        Type.Unknown -> "TV"
    }

internal fun String.toAgeRating(): AgeRating =
    when (this) {
        "R0_PLUS" -> AgeRating.R0Plus
        "R6_PLUS" -> AgeRating.R6Plus
        "R12_PLUS" -> AgeRating.R12Plus
        "R16_PLUS" -> AgeRating.R16Plus
        "R18_PLUS" -> AgeRating.R18Plus
        else -> AgeRating.R0Plus
    }

internal fun AgeRating.toData(): String =
    when (this) {
        AgeRating.R0Plus -> "R0_PLUS"
        AgeRating.R6Plus -> "R6_PLUS"
        AgeRating.R12Plus -> "R12_PLUS"
        AgeRating.R16Plus -> "R16_PLUS"
        AgeRating.R18Plus -> "R18_PLUS"
    }

internal fun Boolean.toPublishStatus(): PublishStatus =
    when (this) {
        true -> PublishStatus.Ongoing
        false -> PublishStatus.Finished
    }

internal fun PublishStatus.toData(): String =
    when (this) {
        PublishStatus.Ongoing -> "IS_ONGOING"
        PublishStatus.Finished -> "IS_NOT_ONGOING"
    }

internal fun String.toCollection(): Collection =
    when (this) {
        "PLANNED" -> Collection.Planned
        "POSTPONED" -> Collection.Postponed
        "WATCHED" -> Collection.Watched
        "WATCHING" -> Collection.Watching
        "ABANDONED" -> Collection.Abandoned
        else -> Collection.Abandoned
    }