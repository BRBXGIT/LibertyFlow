package com.example.network.releases.models.anime_details_item_response

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object representing the professional role of a staff member.
 *
 * @property description A localized description of the role (e.g., 'Translator', 'Voice Actor').
 */
data class RoleDto(
    @field:SerializedName(FIELD_DESCRIPTION)
    val description: String
) {
    companion object Fields {
        private const val FIELD_DESCRIPTION = "description"
    }
}