package com.example.network.common.common_pagination_models.meta

import com.google.gson.annotations.SerializedName

/**
 * DTO containing hypermedia links for API navigation.
 *
 * @property next The URL for the subsequent page of results, if available.
 */
data class LinksDto(
    @field:SerializedName(FIELD_NEXT)
    val next: String? = null
) {
    companion object Fields {
        private const val FIELD_NEXT = "next"
    }
}