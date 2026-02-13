package com.example.network.common.common_pagination.meta

import com.google.gson.annotations.SerializedName

/**
 * Wrapper DTO for the pagination object within the "meta" response field.
 */
data class MetaDto(
    @field:SerializedName(FIELD_PAGINATION)
    val paginationDto: PaginationDto
) {
    companion object Fields {
        private const val FIELD_PAGINATION = "pagination"
    }
}