package com.example.data.models.common.request.request_parameters

import java.time.LocalDateTime

/**
 * Represents a range of years used for filtering content.
 * @property from The starting year of the range (inclusive). Defaults to 0.
 * @property to The ending year of the range (inclusive).
 * Defaults to the current system year via [LocalDateTime].
 */
data class Year(
    val from: Int = 0,
    val to: Int = LocalDateTime.now().year
)
