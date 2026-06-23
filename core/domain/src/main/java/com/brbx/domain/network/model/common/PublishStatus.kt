package com.brbx.domain.network.model.common

enum class PublishStatus(val dataValue: String) {
    Ongoing(dataValue = "IS_ONGOING"),
    Finished(dataValue = "IS_NOT_ONGOING");

    companion object {
        fun fromData(isOngoing: Boolean): PublishStatus {
            return if (isOngoing) Ongoing else Finished
        }
    }
}