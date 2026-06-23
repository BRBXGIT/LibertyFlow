package com.brbx.domain.network.model.common

enum class Sorting(val dataValue: String) {
    CreatedAtDesc(dataValue = "CREATED_AT_DESC"),
    CreatedAtAsc(dataValue = "CREATED_AT_ASC"),
    FreshAtDesc(dataValue = "FRESH_AT_DESC"),
    FreshAtAsc(dataValue = "FRESH_AT_ASC"),
    RatingDesc(dataValue = "RATING_DESC"),
    RatingAsc(dataValue = "RATING_ASC"),
    YearDesc(dataValue = "YEAR_DESC"),
    YearAsc(dataValue = "YEAR_ASC");

    companion object {
        fun fromData(value: String?): Sorting {
            return entries.firstOrNull { it.dataValue == value } ?: CreatedAtDesc
        }
    }
}