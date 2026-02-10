@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.player.components.full_screen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.data.models.player.PlayerSettings
import com.example.design_system.components.bottom_sheets.bs_list.BSList
import com.example.design_system.components.bottom_sheets.bs_list.BSListModel
import com.example.design_system.components.bottom_sheets.bs_list.BSTrailingType
import com.example.design_system.theme.icons.LibertyFlowIcons
import com.example.player.R
import com.example.player.player.PlayerIntent

private data class SettingItemModel(
    override val label: Int,
    override val leadingIcon: Int,
    override val trailingType: BSTrailingType,
    override val onClick: () -> Unit = {},
): BSListModel

@Composable
internal fun SettingsBS(
    playerSettings: PlayerSettings,
    onPlayerIntent: (PlayerIntent) -> Unit
) {
    val settings = remember(playerSettings) {
        listOf(
            SettingItemModel(
                label = R.string.quality_label,
                leadingIcon = LibertyFlowIcons.HighQuality,
                trailingType = BSTrailingType.Navigation,
                onClick = { onPlayerIntent(PlayerIntent.ToggleQualityBS) }
            ),
            SettingItemModel(
                leadingIcon = LibertyFlowIcons.RewindForwardCircle,
                label = R.string.skip_opening_buttons_label,
                trailingType = BSTrailingType.Toggle(playerSettings.showSkipOpeningButton),
                onClick = { onPlayerIntent(PlayerIntent.ToggleShowSkipOpeningButton) }
            ),
            SettingItemModel(
                leadingIcon = LibertyFlowIcons.Rocket,
                label = R.string.auto_skip_opening_label,
                trailingType = BSTrailingType.Toggle(playerSettings.autoSkipOpening),
                onClick = { onPlayerIntent(PlayerIntent.ToggleAutoSkipOpening) }
            ),
            SettingItemModel(
                leadingIcon = LibertyFlowIcons.PlayCircle,
                label = R.string.autoplay_label,
                trailingType = BSTrailingType.Toggle(playerSettings.autoPlay),
                onClick = { onPlayerIntent(PlayerIntent.ToggleAutoPlay) }
            )
        )
    }

    BSList(
        items = settings,
        onDismissRequest = { onPlayerIntent(PlayerIntent.ToggleSettingsBS) }
    )
}