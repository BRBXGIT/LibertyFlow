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
import com.example.design_system.theme.theme.mDimens
import com.example.design_system.theme.theme.mTypography
import com.example.settings.R
import com.example.settings.screen.SettingsIntent

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

private data class ColorSchemePreview(
    val name: Int,
    val lightScheme: ColorScheme,
    val darkScheme: ColorScheme,
    val baseValue: ColorSchemeValue
)

private val CherryLabelRes = R.string.cherry_label
private val TacosLabelRes = R.string.tacos_label
private val LavenderLabelRes = R.string.lavender_label
private val AppleLabelRes = R.string.green_apple_label
private val SakuraLabelRes = R.string.sakura_label
private val SeaLabelRes = R.string.sea_label

private val colorSchemes = listOf(
    ColorSchemePreview(
        name = LavenderLabelRes,
        lightScheme = LightLavenderScheme,
        darkScheme = DarkLavenderScheme,
        baseValue = ColorSchemeValue.LIGHT_LAVENDER_SCHEME
    ),
    ColorSchemePreview(
        name = CherryLabelRes,
        lightScheme = LightCherryColorScheme,
        darkScheme = DarkCherryColorScheme,
        baseValue = ColorSchemeValue.LIGHT_CHERRY_SCHEME
    ),
    ColorSchemePreview(
        name = TacosLabelRes,
        lightScheme = LightTacosScheme,
        darkScheme = DarkTacosScheme,
        baseValue = ColorSchemeValue.LIGHT_TACOS_SCHEME
    ),
    ColorSchemePreview(
        name = SeaLabelRes,
        lightScheme = LightSeaScheme,
        darkScheme = DarkSeaScheme,
        baseValue = ColorSchemeValue.LIGHT_SEA_SCHEME
    ),
    ColorSchemePreview(
        name = AppleLabelRes,
        lightScheme = LightGreenAppleScheme,
        darkScheme = DarkGreenAppleScheme,
        baseValue = ColorSchemeValue.LIGHT_GREEN_APPLE_SCHEME
    ),
    ColorSchemePreview(
        name = SakuraLabelRes,
        lightScheme = LightSakuraScheme,
        darkScheme = DarkSakuraScheme,
        baseValue = ColorSchemeValue.LIGHT_SAKURA_SCHEME
    )
)

private const val LOW_BAR = "_"

/**
 * A horizontally scrollable row that displays color scheme preview cards.
 * * This component evaluates the current [theme] (Light, Dark, or System) to
 * determine which version of the color palettes to display. It handles
 * selection logic by comparing the base color scheme values.
 *
 * @param colorScheme The currently active [ColorSchemeValue] from the application state.
 * @param theme The current [ThemeValue] to determine if previews should look light or dark.
 * @param onIntent Callback to dispatch a [SettingsIntent.SetColorScheme] when a card is clicked.
 */
@Composable
internal fun LazyItemScope.ColorSchemesLR(
    colorScheme: ColorSchemeValue?,
    theme: ThemeValue,
    onIntent: (SettingsIntent) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = mDimens.paddingMedium),
        horizontalArrangement = Arrangement.spacedBy(mDimens.spacingMedium),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .animateItem()
            .fillMaxWidth()
    ) {
        items(colorSchemes) { preview ->
            val isDarkTheme = if (theme == ThemeValue.SYSTEM) {
                isSystemInDarkTheme()
            } else {
                theme != ThemeValue.LIGHT
            }
            val visualColors = if (isDarkTheme) preview.darkScheme else preview.lightScheme

            val targetValue = preview.baseValue.forMode(isDarkTheme)

            val isSelected = colorScheme?.name?.substringAfter(LOW_BAR) ==
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

/**
 * A decorative card representing a specific color theme.
 * * It contains an abstract UI illustration using [scheme] colors (Primary, Secondary,
 * Tertiary, and Surface variants) to give the user a visual "vibe" of the theme
 * before applying it.
 *
 * @param themeName The localized string name of the theme (e.g., 'Sakura').
 * @param scheme The [ColorScheme] containing the actual colors to be rendered.
 * @param isSelected Whether this specific scheme is the one currently active.
 * @param onClick Triggered when the user selects this card.
 */
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
                    .padding(mDimens.paddingSmall),
                verticalArrangement = Arrangement.spacedBy(mDimens.spacingExtraSmall)
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
                    .padding(end = mDimens.paddingSmall)
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