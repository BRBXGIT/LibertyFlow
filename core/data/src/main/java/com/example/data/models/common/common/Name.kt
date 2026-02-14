package com.example.data.models.common.common

import androidx.compose.runtime.Immutable

/**
 * Represents the localized and alternative titles for an anime or character.
 * * This class is marked with [@Immutable], ensuring that UI components displaying
 * these names do not undergo unnecessary recompositions when the state is stable.
 *
 * @property russian The title or name in Russian.
 * @property english The title or name in English.
 * @property alternative An optional secondary or "also known as" name,
 * which may be null if no alternative exists.
 */
@Immutable
data class Name(
    val russian: String,
    val english: String,
    val alternative: String?
)