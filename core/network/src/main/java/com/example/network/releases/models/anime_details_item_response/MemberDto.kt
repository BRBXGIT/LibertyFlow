package com.example.network.releases.models.anime_details_item_response

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object representing a staff member or voice actor
 * associated with an anime release.
 *
 * @property nickname The name or alias of the person.
 * @property roleDto The specific role or position held by the member (e.g., Translator, Voice Actor).
 */
data class MemberDto(
    @field:SerializedName(FIELD_NICKNAME)
    val nickname: String,

    @field:SerializedName(FIELD_ROLE)
    val roleDto: RoleDto
) {
    companion object Fields {
        private const val FIELD_NICKNAME = "nickname"
        private const val FIELD_ROLE = "role"
    }
}