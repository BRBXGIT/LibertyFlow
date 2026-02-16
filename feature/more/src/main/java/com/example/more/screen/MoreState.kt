package com.example.more.screen

import androidx.compose.runtime.Immutable

/**
 * Represents the UI state for the 'More' screen.
 * * Marked as @Immutable to allow Compose to optimize recomposition by
 * skipping UI blocks if the state instance remains the same.
 *
 * @property isLogoutADVisible Controls the visibility of the Logout Alert Dialog (AD).
 */
@Immutable
data class MoreState(
    val isLogoutADVisible: Boolean = false
) {
    fun toggleLogoutAD() = copy(isLogoutADVisible = !isLogoutADVisible)
}
