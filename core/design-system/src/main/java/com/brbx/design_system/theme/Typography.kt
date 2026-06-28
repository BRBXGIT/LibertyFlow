package com.brbx.design_system.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.brbx.design_system.common.DesignSystemFonts

private val fontFamily = FontFamily(
    Font(DesignSystemFonts.manrope_variable)
)

val Typography = Typography(
    displayLarge = TextStyle(
        fontWeight = FontWeight.W700,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp,
        fontFamily = fontFamily
    ),
    displayMedium = TextStyle(
        fontWeight = FontWeight.W700,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp,
        fontFamily = fontFamily
    ),
    displaySmall = TextStyle(
        fontWeight = FontWeight.W700,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp,
        fontFamily = fontFamily
    ),
    headlineLarge = TextStyle(
        fontWeight = FontWeight.W700,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp,
        fontFamily = fontFamily
    ),
    headlineMedium = TextStyle(
        fontWeight = FontWeight.W700,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp,
        fontFamily = fontFamily
    ),
    headlineSmall = TextStyle(
        fontWeight = FontWeight.W700,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp,
        fontFamily = fontFamily
    ),
    titleLarge = TextStyle(
        fontWeight = FontWeight.W700,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
        fontFamily = fontFamily
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.W700,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
        fontFamily = fontFamily
    ),
    titleSmall = TextStyle(
        fontWeight = FontWeight.W700,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp,
        fontFamily = fontFamily
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.W700,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
        fontFamily = fontFamily
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.W700,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp,
        fontFamily = fontFamily
    ),
    bodySmall = TextStyle(
        fontWeight = FontWeight.W700,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp,
        fontFamily = fontFamily
    ),
    labelLarge = TextStyle(
        fontWeight = FontWeight.W700,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp,
        fontFamily = fontFamily
    ),
    labelMedium = TextStyle(
        fontWeight = FontWeight.W700,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp,
        fontFamily = fontFamily
    ),
    labelSmall = TextStyle(
        fontWeight = FontWeight.W700,
        fontSize = 10.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp,
        fontFamily = fontFamily
    )
)