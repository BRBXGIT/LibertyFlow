package com.example.network.releases.models.anime_details_item_response


import com.google.gson.annotations.SerializedName

data class MemberDto(
    @SerializedName("nickname") val nickname: String,
    @SerializedName("role") val roleDto: RoleDto
)