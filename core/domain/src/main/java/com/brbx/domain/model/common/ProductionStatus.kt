package com.brbx.domain.model.common

enum class ProductionStatus(val dataValue: String) {
    InProduction(dataValue = "IS_IN_PRODUCTION"),
    Finished(dataValue = "IS_NOT_IN_PRODUCTION");

    companion object {
        fun fromData(value: String?): ProductionStatus {
            return entries.firstOrNull { it.dataValue == value } ?: Finished
        }
    }
}