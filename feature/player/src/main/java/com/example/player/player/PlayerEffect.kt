package com.example.player.player

/**
 * Represents one-time side effects emitted by the [PlayerVM].
 * * Unlike [PlayerState], which represents a persistent condition of the UI,
 * [PlayerEffect] is used for "fire-and-forget" actions that the UI should
 * handle only once (e.g., navigation, showing toasts, or system-level requests).
 */
sealed interface PlayerEffect {
    // --- Player ---
    data object TryPipEnterPip: PlayerEffect
}