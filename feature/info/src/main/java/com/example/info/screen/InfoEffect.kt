package com.example.info.screen

/**
 * Represents one-time side effects or UI events related to the Information screen.
 */
sealed interface InfoEffect {
    data class CopyVersion(val versionTextRes: Int): InfoEffect
}