package com.example.network.common.common_pagination.meta

import com.google.gson.annotations.SerializedName

data class MetaDto(
    @SerializedName("pagination") val paginationDto: PaginationDto
)