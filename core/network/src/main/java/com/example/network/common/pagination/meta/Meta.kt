package com.example.network.common.pagination.meta


import com.google.gson.annotations.SerializedName

data class Meta(
    @SerializedName("pagination") val pagination: Pagination
)