package com.example.data.models.common.common

import androidx.compose.runtime.Immutable

/**
 * Represents a specific category or genre associated with anime content.
 *
 * This class is marked as [@Immutable] to ensure the Compose compiler treats it
 * as stable, even if it's passed as a parameter to a Composable function.
 *
 * @property id The unique identifier for the genre.
 * @property name The display name of the genre (e.g., "Action", "Sci-Fi", "Slice of Life").
 */
@Immutable
data class Genre(
    val id: Int,
    val name: String
)
