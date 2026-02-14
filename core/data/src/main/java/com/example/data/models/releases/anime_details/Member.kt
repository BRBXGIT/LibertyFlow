package com.example.data.models.releases.anime_details

/**
 * Represents a person involved in the production of an anime.
 *
 * This includes both the creative staff (directors, animators) and
 * the cast (voice actors/Seiyuu).
 *
 * @property name The full name of the staff member or voice actor.
 * @property role The specific responsibility or character voiced by the member
 * (e.g., 'Voice Actor', 'Translator').
 */
data class Member(
    val name: String,
    val role: String
)