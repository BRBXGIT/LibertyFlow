package com.example.data.models.theme

/**
 * Represents the distinct color palettes available in the app.
 * Each entry knows its "mode" (Dark/Light) and can switch to its opposite.
 */
enum class ColorSchemeValue {
    DARK_CHERRY_SCHEME,
    LIGHT_CHERRY_SCHEME,
    LIGHT_TACOS_SCHEME,
    DARK_TACOS_SCHEME,
    LIGHT_LAVENDER_SCHEME,
    DARK_LAVENDER_SCHEME,
    LIGHT_GREEN_APPLE_SCHEME,
    DARK_GREEN_APPLE_SCHEME,
    LIGHT_SAKURA_SCHEME,
    DARK_SAKURA_SCHEME,
    LIGHT_SEA_SCHEME,
    DARK_SEA_SCHEME;

    /**
     * Returns true if this scheme is a dark variant.
     */
    val isDark: Boolean
        get() = name.startsWith("DARK_")

    /**
     * Returns the opposite version of the current scheme.
     * e.g., DARK_CHERRY -> LIGHT_CHERRY
     */
    fun toggleMode(): ColorSchemeValue {
        return when {
            isDark -> entries.firstOrNull { it.name == name.replace("DARK_", "LIGHT_") } ?: this
            else -> entries.firstOrNull { it.name == name.replace("LIGHT_", "DARK_") } ?: this
        }
    }

    /**
     * Forces the scheme to match a specific mode (Dark/Light).
     */
    fun forMode(forceDark: Boolean): ColorSchemeValue {
        return if (forceDark && !isDark) toggleMode()
        else if (!forceDark && isDark) toggleMode()
        else this
    }
}