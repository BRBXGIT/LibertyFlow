@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.settings.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import com.example.common.ui_helpers.effects.UiEffect
import com.example.data.models.player.VideoQuality
import com.example.data.models.theme.LibertyFlowTheme
import com.example.data.models.theme.TabType
import com.example.data.models.theme.ThemeValue
import com.example.design_system.components.bars.basic_top_bar.LibertyFlowBasicTopBar
import com.example.design_system.components.dividers.dividerWithLabel
import com.example.design_system.components.list_tems.M3ListItem
import com.example.design_system.theme.icons.LibertyFlowIcons
import com.example.design_system.theme.theme.mDimens
import com.example.settings.R
import com.example.settings.components.ColorSchemesLR
import com.example.settings.components.SegmentedThemeButton
import com.example.settings.components.VideoQualityBS

private val SettingsLabelRes = R.string.settings_label

private val ThemeLabelRes = R.string.theme_label
private val PlayerLabelRes = R.string.player_label
private val OtherLabelRes = R.string.other_label

/**
 * A private data model representing a single row in the settings list.
 * @property icon The drawable resource for the setting's leading icon.
 * @property labelRes The string resource for the primary title.
 * @property descriptionRes Optional string resource for secondary descriptive text.
 * @property isEnabled If non-null, renders a toggle/switch representing the boolean state.
 * @property intent The [SettingsIntent] to be dispatched when this item is interacted with.
 */
@Immutable
private data class Setting(
    val icon: Int,
    val labelRes: Int,
    val descriptionRes: Int? = null,
    val isEnabled: Boolean? = null,
    val quality: VideoQuality? = null,
    val intent: SettingsIntent
)

private val QualityLabelRes = R.string.quality_label
private val ShowSkipOpeningButtonLabelRes = R.string.show_skip_opening_button_label
private val AutoSkipOpeningLabelRes = R.string.auto_skip_opening_label
private val AutoPlayLabelRes = R.string.auto_play_label
private val CropLabelRes = R.string.crop_label

private val UseExpressiveLabelRes = R.string.use_expressive_label
private val TabStyleLabelRes = R.string.tab_type_label
private val TabletStyleLabelRes = R.string.tablet_style_label
private val M3StyleLabelRes = R.string.m3_style_label
private val ChipsStyleLabelRes = R.string.chips_style_label

/**
 * The primary entry point for the Settings UI.
 * * This screen provides a centralized interface for users to customize:
 * 1. **Visual Identity**: Theme preference (Light/Dark/System) and dynamic color schemes.
 * 2. **Playback Experience**: Video quality, auto-play, and skipping preferences.
 * 3. **Interface Style**: Tab navigation styles (M3, Tablet, or Chips) and expressive layouts.
 * * It features a [nestedScroll] connection for smooth TopAppBar transitions and utilizes
 * [remember] blocks to optimize the performance of setting list generation.
 *
 * @param state The [SettingsState] containing current user preferences and UI visibility flags.
 * @param onIntent Lambda to process user interactions (e.g., toggling a switch).
 * @param onEffect Lambda to trigger one-time UI events (e.g., navigating back).
 */
@Composable
fun Settings(
    state: SettingsState,
    onIntent: (SettingsIntent) -> Unit,
    onEffect: (UiEffect) -> Unit
) {
    val topBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        topBar = {
            LibertyFlowBasicTopBar(
                label = stringResource(SettingsLabelRes),
                onNavClick = { onEffect(UiEffect.NavigateBack) },
                scrollBehavior = topBarScrollBehavior
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(topBarScrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        if (state.isQualityBSVisible) {
            VideoQualityBS(
                onDismissRequest = { onIntent(SettingsIntent.ToggleIsQualityBSVisible) },
                onItemClick = { onIntent(SettingsIntent.SetQuality(it)) },
                selectedQuality = state.playerSettings.quality
            )
        }

        val playerSettings = remember(
            key1 = state.playerSettings,
            key2 = state.themeSettings.useExpressive
        ) {
            listOf(
                Setting(
                    icon = LibertyFlowIcons.Outlined.HighQuality,
                    labelRes = QualityLabelRes,
                    quality = state.playerSettings.quality,
                    intent = SettingsIntent.ToggleIsQualityBSVisible
                ),
                Setting(
                    icon = LibertyFlowIcons.Outlined.PlayCircle,
                    labelRes = AutoPlayLabelRes,
                    isEnabled = state.playerSettings.autoPlay,
                    intent = SettingsIntent.ToggleAutoPlay
                ),
                Setting(
                    icon = LibertyFlowIcons.Outlined.RewindForwardCircle,
                    labelRes = ShowSkipOpeningButtonLabelRes,
                    isEnabled = state.playerSettings.showSkipOpeningButton,
                    intent = SettingsIntent.ToggleShowSkipOpeningButton
                ),
                Setting(
                    icon = LibertyFlowIcons.Outlined.Rocket,
                    labelRes = AutoSkipOpeningLabelRes,
                    isEnabled = state.playerSettings.autoSkipOpening,
                    intent = SettingsIntent.ToggleAutoSkipOpening
                ),
                Setting(
                    icon = LibertyFlowIcons.Outlined.Crop,
                    labelRes = CropLabelRes,
                    isEnabled = state.playerSettings.isCropped,
                    intent = SettingsIntent.ToggleIsCropped
                )
            )
        }

        val commonSettings = remember(
            key1 = state.themeSettings.useExpressive,
            key2 = state.themeSettings.tabType
        ) {
            listOf(
                Setting(
                    icon = LibertyFlowIcons.Outlined.Tablet,
                    labelRes = TabStyleLabelRes,
                    descriptionRes = when(state.themeSettings.tabType) {
                        TabType.M3 -> M3StyleLabelRes
                        TabType.Tablet -> TabletStyleLabelRes
                        TabType.Chips -> ChipsStyleLabelRes
                    },
                    intent = SettingsIntent.ToggleTabType
                ),
                Setting(
                    icon = LibertyFlowIcons.Outlined.Colour,
                    labelRes = UseExpressiveLabelRes,
                    isEnabled = state.themeSettings.useExpressive,
                    intent = SettingsIntent.ToggleUseExpressive
                )
            )
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(mDimens.spacingMedium),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                bottom = innerPadding.calculateBottomPadding() + mDimens.paddingMedium,
                top = innerPadding.calculateTopPadding() + mDimens.paddingMedium
            )
        ) {
            dividerWithLabel(ThemeLabelRes)

            theme(state, onIntent)

            colorScheme(state.themeSettings, onIntent)

            dividerWithLabel(PlayerLabelRes)

            settingsList(playerSettings, onIntent)

            dividerWithLabel(OtherLabelRes)

            settingsList(commonSettings, onIntent)
        }
    }
}

private const val THEME_KEY = "ThemeKey"

private fun LazyListScope.theme(
    state: SettingsState,
    onIntent: (SettingsIntent) -> Unit
) {
    item(
        key = THEME_KEY
    ) {
        SegmentedThemeButton(
            onIntent = onIntent,
            theme = state.themeSettings.userThemePreference
        )
    }
}

private const val COLOR_SCHEME_KEY = "ColorSchemeKey"

private fun LazyListScope.colorScheme(
    theme: LibertyFlowTheme,
    onIntent: (SettingsIntent) -> Unit
) {
    if (theme.userThemePreference != ThemeValue.DYNAMIC) {
        item(
            key = COLOR_SCHEME_KEY
        ) {
            ColorSchemesLR(
                theme = theme.userThemePreference,
                onIntent = onIntent,
                colorScheme = theme.activeColorScheme
            )
        }
    }
}

private fun LazyListScope.settingsList(
    settings: List<Setting>,
    onIntent: (SettingsIntent) -> Unit
) {
    items(
        items = settings,
        key = { setting -> setting.labelRes }
    ) { setting ->
        M3ListItem(
            description = if (setting.descriptionRes == null) null else stringResource(setting.descriptionRes),
            title = stringResource(setting.labelRes),
            onClick = { onIntent(setting.intent) },
            modifier = Modifier.animateItem(),
            icon = setting.icon,
            isChecked = setting.isEnabled
        )
    }
}