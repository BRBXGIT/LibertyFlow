package com.example.network.common.common_request_models.request_parameters

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object representing a range of years for filtering anime releases.
 * @property fromYear The starting year of the range (inclusive).
 * @property toYear The ending year of the range (inclusive).
 */
data class YearsDto(
    @field:SerializedName(FIELD_FROM_YEAR)
    val fromYear: Int,

    @field:SerializedName(FIELD_TO_YEAR)
    val toYear: Int
) {
    companion object Fields {
        private const val FIELD_FROM_YEAR = "from_year"
        private const val FIELD_TO_YEAR = "to_year"
    }
}