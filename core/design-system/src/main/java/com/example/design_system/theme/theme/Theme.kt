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

@Composable
fun LibertyFlowTheme(
    theme: ThemeValue = ThemeValue.SYSTEM,
    colorScheme: ColorSchemeValue = ColorSchemeValue.DARK_LAVENDER_SCHEME,
    useExpressive: Boolean = false,
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
        motionScheme = if (useExpressive) MotionScheme.expressive() else MotionScheme.standard()
    )
}

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


val mColors @Composable get() = MaterialTheme.colorScheme
val mTypography @Composable get() = MaterialTheme.typography
val mShapes @Composable get() = MaterialTheme.shapes
val mMotionScheme @Composable get() = MaterialTheme.motionScheme