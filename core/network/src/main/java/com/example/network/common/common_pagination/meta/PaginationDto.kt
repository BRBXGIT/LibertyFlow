package com.example.network.common.common_pagination.meta

import com.google.gson.annotations.SerializedName

data class PaginationDto(
    @SerializedName("count") val count: Int,
    @SerializedName("current_page") val currentPage: Int,
    @SerializedName("links") val linksDto: LinksDto,
    @SerializedName("per_page") val perPage: Int,
    @SerializedName("total") val total: Int,
    @SerializedName("total_pages") val totalPages: Int
)