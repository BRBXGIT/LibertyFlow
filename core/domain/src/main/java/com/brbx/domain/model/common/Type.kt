package com.brbx.domain.model.common

enum class Type(val dataValue: String) {
    TV(dataValue = "TV"),
    ONA(dataValue = "ONA"),
    Web(dataValue = "WEB"),
    OVA(dataValue = "OVA"),
    OAD(dataValue = "OAD"),
    Movie(dataValue = "MOVIE"),
    Dorama(dataValue = "DORAMA"),
    Special(dataValue = "SPECIAL"),
    Unknown(dataValue = "TV");

    companion object {
        fun fromData(value: String?): Type {
            return entries.firstOrNull { it.dataValue == value } ?: Unknown
        }
    }
}