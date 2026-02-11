package com.example.data.models.common.request.request_parameters

enum class Type { TV, ONA, WEB, OVA, OAD, MOVIE, DORAMA, SPECIAL }

enum class Season { WINTER, AUTUMN, SPRING, SUMMER }

enum class AgeRating { R6_PLUS, R12_PLUS, R16_PLUS, R18_PLUS, R21_PLUS }

enum class Sorting {
    CREATED_AT_DESC,
    CREATED_AT_ASC,
    FRESH_AT_DESC,
    FRESH_AT_ASC,
    RATING_DESC,
    RATING_ASC,
    YEAR_DESC,
    YEAR_ASC
}

enum class PublishStatus { IS_ONGOING, IS_NOT_ONGOING }

enum class ProductionsStatus { IS_IN_PRODUCTION, IS_NOT_IN_PRODUCTION }

enum class Collection { WATCHING, PLANNED, POSTPONED, ABANDONED, WATCHED }