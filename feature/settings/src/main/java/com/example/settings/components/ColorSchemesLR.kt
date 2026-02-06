package com.example.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.data.models.theme.ColorSchemeValue
import com.example.data.models.theme.LibertyFlowTheme
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
import com.example.design_system.theme.theme.mTypography
import com.example.settings.R
import com.example.settings.screen.SettingsIntent

// --- Layout Constants ---
private val LRArrangement = 16.dp
private val LRPadding = 16.dp

// --- Card Dimensions ---
private val CardWidth = 140.dp
private val CardCorner = 20.dp
private val CardBorderSelected = 2.dp
private val CardBorderUnselected = 1.dp
private val LabelVerticalPadding = 12.dp

private const val HALF_PERCENT_ALPHA = 0.5f
private const val SIXTEEN_PRECENTS_ALPHA = 0.6f

// --- Preview Section Dimensions ---
private val PreviewHeight = 100.dp
private val PreviewPadding = 12.dp
private val PreviewCorner = 12.dp

// --- Abstract Shapes Dimensions ---
private val PrimaryCircleSize = 40.dp
private val PrimaryCircleOffset = (-10).dp

private val TertiaryAccentSize = 16.dp
private val TertiaryAccentOffset = (-12).dp

private val TextBarHeight = 6.dp
private val TextBarLongWidth = 40.dp
private val TextBarShortWidth = 25.dp
private val TextBarCorner = 2.dp
private val TextBarSpacing = 4.dp
private val ContentPadding = 8.dp

private data class ColorSchemePreview(
    val name: Int,
    val lightScheme: ColorScheme,
    val darkScheme: ColorScheme,
    val baseValue: ColorSchemeValue
)

private val CherryLabel = R.string.cherry_label
private val TacosLabel = R.string.tacos_label
private val LavenderLabel = R.string.lavender_label
private val AppleLabel = R.string.green_apple_label
private val SakuraLabel = R.string.sakura_label
private val SeaLabel = R.string.sea_label

private val colorSchemes = listOf(
    ColorSchemePreview(
        name = LavenderLabel,
        lightScheme = LightLavenderScheme,
        darkScheme = DarkLavenderScheme,
        baseValue = ColorSchemeValue.LIGHT_LAVENDER_SCHEME
    ),
    ColorSchemePreview(
        name = CherryLabel,
        lightScheme = LightCherryColorScheme,
        darkScheme = DarkCherryColorScheme,
        baseValue = ColorSchemeValue.LIGHT_CHERRY_SCHEME
    ),
    ColorSchemePreview(
        name = TacosLabel,
        lightScheme = LightTacosScheme,
        darkScheme = DarkTacosScheme,
        baseValue = ColorSchemeValue.LIGHT_TACOS_SCHEME
    ),
    ColorSchemePreview(
        name = SeaLabel,
        lightScheme = LightSeaScheme,
        darkScheme = DarkSeaScheme,
        baseValue = ColorSchemeValue.LIGHT_SEA_SCHEME
    ),
    ColorSchemePreview(
        name = AppleLabel,
        lightScheme = LightGreenAppleScheme,
        darkScheme = DarkGreenAppleScheme,
        baseValue = ColorSchemeValue.LIGHT_GREEN_APPLE_SCHEME
    ),
    ColorSchemePreview(
        name = SakuraLabel,
        lightScheme = LightSakuraScheme,
        darkScheme = DarkSakuraScheme,
        baseValue = ColorSchemeValue.LIGHT_SAKURA_SCHEME
    )
)

private const val LOW_BAR = "_"

@Composable
internal fun LazyItemScope.ColorSchemesLR(
    theme: LibertyFlowTheme,
    onIntent: (SettingsIntent) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = LRPadding),
        horizontalArrangement = Arrangement.spacedBy(LRArrangement),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .animateItem()
            .fillMaxWidth()
    ) {
        items(colorSchemes) { preview ->
            val isDarkTheme = if (theme.userThemePreference == ThemeValue.SYSTEM) {
                isSystemInDarkTheme()
            } else {
                theme.userThemePreference != ThemeValue.LIGHT
            }
            val visualColors = if (isDarkTheme) preview.darkScheme else preview.lightScheme

            val targetValue = preview.baseValue.forMode(isDarkTheme)

            val isSelected = theme.activeColorScheme?.name?.substringAfter(LOW_BAR) ==
                    targetValue.name.substringAfter(LOW_BAR)

            ColorSchemePreviewCard(
                themeName = stringResource(preview.name),
                scheme = visualColors,
                isSelected = isSelected,
                onClick = { onIntent(SettingsIntent.SetColorScheme(targetValue)) }
            )
        }
    }
}

@Composable
private fun ColorSchemePreviewCard(
    themeName: String,
    scheme: ColorScheme,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    // Dynamic border properties based on selection state
    val borderColor = if (isSelected) scheme.primary else scheme.outline.copy(alpha = HALF_PERCENT_ALPHA)
    val borderThickness = if (isSelected) CardBorderSelected else CardBorderUnselected

    Column(
        modifier = Modifier
            .width(CardWidth)
            .clip(RoundedCornerShape(CardCorner))
            .clickable(onClick = onClick)
            .border(borderThickness, borderColor, RoundedCornerShape(CardCorner))
            .background(scheme.surface),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Abstract UI Illustration Container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(PreviewHeight)
                .padding(PreviewPadding)
                .clip(RoundedCornerShape(PreviewCorner))
                .background(scheme.surfaceVariant)
        ) {
            // Background Decorative Shape (Primary)
            Box(
                modifier = Modifier
                    .size(PrimaryCircleSize)
                    .offset(x = PrimaryCircleOffset, y = PrimaryCircleOffset)
                    .background(scheme.primary, CircleShape)
                    .align(Alignment.TopStart)
            )

            // Abstract Text Lines (Secondary)
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(ContentPadding),
                verticalArrangement = Arrangement.spacedBy(TextBarSpacing)
            ) {
                Box(
                    Modifier
                        .width(TextBarLongWidth)
                        .height(TextBarHeight)
                        .background(scheme.secondary, RoundedCornerShape(TextBarCorner))
                )
                Box(
                    Modifier
                        .width(TextBarShortWidth)
                        .height(TextBarHeight)
                        .background(scheme.secondary.copy(alpha = SIXTEEN_PRECENTS_ALPHA), RoundedCornerShape(TextBarCorner))
                )
            }

            // Focal Point Accent (Tertiary)
            Box(
                modifier = Modifier
                    .padding(end = ContentPadding)
                    .size(TertiaryAccentSize)
                    .background(scheme.tertiary, CircleShape)
                    .align(Alignment.CenterEnd)
                    .offset(x = TertiaryAccentOffset)
            )
        }

        // Theme Title Label
        Text(
            text = themeName,
            style = mTypography.bodyLarge,
            color = if (isSelected) scheme.primary else scheme.onSurface,
            modifier = Modifier.padding(vertical = LabelVerticalPadding)
        )
    }
}