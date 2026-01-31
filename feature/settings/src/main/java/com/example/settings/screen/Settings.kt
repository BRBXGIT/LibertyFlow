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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.common.ui_helpers.effects.UiEffect
import com.example.data.models.player.VideoQuality
import com.example.data.models.theme.LibertyFlowTheme
import com.example.data.models.theme.ThemeValue
import com.example.design_system.components.bars.basic_top_bar.BasicTopBar
import com.example.design_system.components.bottom_sheets.player_settings.VideoQualityBS
import com.example.design_system.components.dividers.dividerWithLabel
import com.example.design_system.components.list_tems.M3ListItem
import com.example.design_system.theme.LibertyFlowIcons
import com.example.settings.R
import com.example.settings.components.ColorSchemesLR
import com.example.settings.components.SegmentedThemeButton

private val LCPadding = 16.dp

private val LCSpacedBy = 16.dp

private val SettingsLabel = R.string.settings_label

private val ThemeLabel = R.string.theme_label
private val PlayerLabel = R.string.player_label
private val CommonLabel = R.string.common_label

private data class Setting(
    val icon: Int,
    val labelRes: Int,
    val descriptionRes: Int? = null,
    val isEnabled: Boolean? = null,
    val quality: VideoQuality? = null,
    val intent: SettingsIntent
)

private val QualityLabel = R.string.quality_label
private val ShowSkipOpeningButtonLabel = R.string.show_skip_opening_button_label
private val AutoSkipOpeningLabel = R.string.auto_skip_opening_label
private val AutoPlayLabel = R.string.auto_play_label
private val UseExprssiveLabel = R.string.use_expressive_label

@Composable
fun Settings(
    state: SettingsState,
    onIntent: (SettingsIntent) -> Unit,
    onEffect: (UiEffect) -> Unit
) {
    val topBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        topBar = {
            BasicTopBar(
                label = stringResource(SettingsLabel),
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
                    icon = LibertyFlowIcons.HighQuality,
                    labelRes = QualityLabel,
                    quality = state.playerSettings.quality,
                    intent = SettingsIntent.ToggleIsQualityBSVisible
                ),
                Setting(
                    icon = LibertyFlowIcons.PlayCircle,
                    labelRes = AutoPlayLabel,
                    isEnabled = state.playerSettings.autoPlay,
                    intent = SettingsIntent.ToggleAutoPlay
                ),
                Setting(
                    icon = LibertyFlowIcons.RewindForwardCircle,
                    labelRes = ShowSkipOpeningButtonLabel,
                    isEnabled = state.playerSettings.showSkipOpeningButton,
                    intent = SettingsIntent.ToggleShowSkipOpeningButton
                ),
                Setting(
                    icon = LibertyFlowIcons.Rocket,
                    labelRes = AutoSkipOpeningLabel,
                    isEnabled = state.playerSettings.autoSkipOpening,
                    intent = SettingsIntent.ToggleAutoSkipOpening
                )
            )
        }

        val commonSettings = remember(state.themeSettings.useExpressive) {
            listOf(
                Setting(
                    icon = LibertyFlowIcons.Colour,
                    labelRes = UseExprssiveLabel,
                    isEnabled = state.themeSettings.useExpressive,
                    intent = SettingsIntent.ToggleUseExpressive
                )
            )
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(LCSpacedBy),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                bottom = innerPadding.calculateBottomPadding() + LCPadding,
                top = innerPadding.calculateTopPadding() + LCPadding
            )
        ) {
            dividerWithLabel(ThemeLabel)

            theme(state, onIntent)

            colorScheme(state.themeSettings, onIntent)

            dividerWithLabel(PlayerLabel)

            settingsList(playerSettings, onIntent)

            dividerWithLabel(CommonLabel)

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
            state = state
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
                theme = theme,
                onIntent = onIntent
            )
        }
    }
}

private fun LazyListScope.settingsList(
    settings: List<Setting>,
    onIntent: (SettingsIntent) -> Unit
) {
    items(settings) { setting ->
        M3ListItem(
            title = stringResource(setting.labelRes),
            onClick = { onIntent(setting.intent) },
            modifier = Modifier.animateItem(),
            icon = setting.icon,
            isChecked = setting.isEnabled
        )
    }
}