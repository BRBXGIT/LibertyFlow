package com.brbx.domain.model.common

enum class Season(val dataValue: String) {
    Winter(dataValue = "winter"),
    Spring(dataValue = "spring"),
    Summer(dataValue = "summer"),
    Autumn(dataValue = "autumn"),
    Unknown(dataValue = "");

    companion object {
        fun fromData(value: String?): Season {
            return entries.firstOrNull { it.dataValue == value } ?: Unknown
        }
    }
}