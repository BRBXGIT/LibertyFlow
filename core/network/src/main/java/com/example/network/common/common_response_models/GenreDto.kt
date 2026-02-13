package com.example.network.common.common_response_models

import com.google.gson.annotations.SerializedName

/**
 * DTO representing a genre category.
 */
data class GenreDto(
    @field:SerializedName(FIELD_NAME)
    val name: String,

    @field:SerializedName(FIELD_ID)
    val id: Int
) {
    companion object Fields {
        private const val FIELD_NAME = "name"
        private const val FIELD_ID = "id"
    }
}