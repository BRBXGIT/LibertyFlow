package com.brbx.domain.network.model.common

typealias DomainCollection = Collection

enum class Collection(val dataValue: String) {
    Planned(dataValue = "PLANNED"),
    Watched(dataValue = "WATCHED"),
    Watching(dataValue = "WATCHING"),
    Postponed(dataValue = "POSTPONED"),
    Abandoned(dataValue = "ABANDONED");

    companion object {
        fun fromData(value: String?): Collection {
            return entries.firstOrNull { it.dataValue == value } ?: Abandoned
        }
    }
}