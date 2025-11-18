package com.example.network.common.common_pagination.meta

import com.google.gson.annotations.SerializedName

data class Pagination(
    @SerializedName("count") val count: Int,
    @SerializedName("current_page") val currentPage: Int,
    @SerializedName("links") val links: Links,
    @SerializedName("per_page") val perPage: Int,
    @SerializedName("total") val total: Int,
    @SerializedName("total_pages") val totalPages: Int
) {
    fun nextPage() = if (currentPage < totalPages) currentPage + 1 else null

    fun previousPage() = if (currentPage > 1) currentPage - 1 else null
}