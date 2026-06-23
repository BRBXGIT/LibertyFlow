package com.brbx.domain.model.common

enum class AgeRating(val dataValue: String) {
    R0Plus(dataValue = "R0_PLUS"),
    R6Plus(dataValue = "R6_PLUS"),
    R12Plus(dataValue = "R12_PLUS"),
    R16Plus(dataValue = "R16_PLUS"),
    R18Plus(dataValue = "R18_PLUS");

    companion object {
        fun fromData(value: String?): AgeRating {
            return entries.firstOrNull { it.dataValue == value } ?: R0Plus
        }
    }
}