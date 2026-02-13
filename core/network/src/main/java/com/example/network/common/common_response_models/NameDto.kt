package com.example.network.common.common_response_models

import com.google.gson.annotations.SerializedName

/**
 * DTO containing localized and alternative titles for an anime.
 *
 * @property main The primary title RU title.
 * @property english The official English title.
 * @property alternative Optional string of secondary titles/aliases.
 */
data class NameDto(
    @field:SerializedName(FIELD_MAIN)
    val main: String,

    @field:SerializedName(FIELD_ENGLISH)
    val english: String,

    @field:SerializedName(FIELD_ALTERNATIVE)
    val alternative: String?
) {
    companion object Fields {
        private const val FIELD_MAIN = "main"
        private const val FIELD_ENGLISH = "english"
        private const val FIELD_ALTERNATIVE = "alternative"
    }
}