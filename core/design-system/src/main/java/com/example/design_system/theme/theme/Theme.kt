@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.example.design_system.theme.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import com.example.data.models.theme.ColorSchemeValue
import com.example.data.models.theme.ThemeValue
import com.example.design_system.theme.colors.DarkCherryColorScheme
import com.example.design_system.theme.colors.DarkGreenAppleScheme
import com.example.design_system.theme.colors.DarkLavenderScheme
import com.example.design_system.theme.colors.DarkSakuraScheme
import com.example.design_system.theme.colors.DarkSeaScheme
import com.example.design_system.theme.colors.DarkTacosScheme
import com.example.design_system.theme.colors.LightCherryColorScheme
import com.example.design_system.theme.colors.LightGreenAppleScheme
import com.example.design_system.theme.colors.LightLavenderScheme
import com.example.design_system.theme.colors.LightSakuraScheme
import com.example.design_system.theme.colors.LightSeaScheme
import com.example.design_system.theme.colors.LightTacosScheme

/**
 * The primary [MaterialTheme] wrapper for the LibertyFlow application.
 * * This theme manages:
 * 1. **Color Schemes**: Supports dynamic Material You colors (Android 12+) and a variety of
 * custom preset schemes.
 * 2. **Motion**: Toggles between standard and expressive motion schemes.
 * 3. **Custom Tokens**: Provides [LocalDimensions] and [LocalAnimationTokens] via
 * [CompositionLocalProvider] for use in lower-level composables.
 *
 * @param animationTokens Custom motion and timing values for the application.
 * @param dimensions Custom spacing, sizing, and layout constants.
 * @param theme Determines if the theme follows system settings or uses [ThemeValue.DYNAMIC].
 * @param colorScheme The specific color palette to apply when not using dynamic colors.
 * @param useExpressive If true, applies the [MotionScheme.expressive] configuration.
 * @param content The composable UI hierarchy that will inherit this theme.
 */
@Composable
fun LibertyFlowTheme(
    // Dimens
    animationTokens: LibertyFlowAnimationTokens = LibertyFlowAnimationTokens(),
    dimensions: LibertyFlowDimensions = LibertyFlowDimensions(),
    // Theme
    theme: ThemeValue = ThemeValue.SYSTEM,
    colorScheme: ColorSchemeValue = ColorSchemeValue.DARK_LAVENDER_SCHEME,
    useExpressive: Boolean = false,
    // Content
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val colorScheme = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (theme == ThemeValue.DYNAMIC) {
            if (isSystemInDarkTheme()) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        } else {
            getColorScheme(colorScheme)
        }
    } else {
        getColorScheme(colorScheme)
    }

    CompositionLocalProvider(
        LocalDimensions provides dimensions,
        LocalAnimationTokens provides animationTokens
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content,
            motionScheme = if (useExpressive) MotionScheme.expressive() else MotionScheme.standard()
        )
    }
}

/**
 * Returns the appropriate [ColorScheme] based on the provided [ColorSchemeValue] enum.
 */
@Composable
private fun getColorScheme(colorSchemeValue: ColorSchemeValue): ColorScheme {
    return when (colorSchemeValue) {
        ColorSchemeValue.DARK_CHERRY_SCHEME -> DarkCherryColorScheme
        ColorSchemeValue.LIGHT_CHERRY_SCHEME -> LightCherryColorScheme

        ColorSchemeValue.LIGHT_TACOS_SCHEME -> LightTacosScheme
        ColorSchemeValue.DARK_TACOS_SCHEME -> DarkTacosScheme

        ColorSchemeValue.LIGHT_LAVENDER_SCHEME -> LightLavenderScheme
        ColorSchemeValue.DARK_LAVENDER_SCHEME -> DarkLavenderScheme

        ColorSchemeValue.LIGHT_GREEN_APPLE_SCHEME -> LightGreenAppleScheme
        ColorSchemeValue.DARK_GREEN_APPLE_SCHEME -> DarkGreenAppleScheme

        ColorSchemeValue.LIGHT_SAKURA_SCHEME -> LightSakuraScheme
        ColorSchemeValue.DARK_SAKURA_SCHEME -> DarkSakuraScheme

        ColorSchemeValue.LIGHT_SEA_SCHEME -> LightSeaScheme
        ColorSchemeValue.DARK_SEA_SCHEME -> DarkSeaScheme
    }
}

// Quick access to theme values
val mColors @Composable get() = MaterialTheme.colorScheme
val mTypography @Composable get() = MaterialTheme.typography
val mShapes @Composable get() = MaterialTheme.shapes
val mMotionScheme @Composable get() = MaterialTheme.motionScheme
val mDimens @Composable get() = LocalDimensions.current
val mAnimationTokens @Composable get() = LocalAnimationTokens.current