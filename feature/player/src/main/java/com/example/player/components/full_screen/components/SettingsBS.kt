@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.player.components.full_screen.components

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

/**
 * A internal data model representing an entry in the settings bottom sheet.
 * @property text String resource ID for the setting's display name.
 * @property leadingIcon Icon resource ID displayed at the start of the row.
 * @property trailingType Defines the interactive element at the end of the row
 * (e.g., a toggle switch or a navigation arrow).
 * @property onClick The action to execute when the setting row is tapped.
 */
private data class SettingItemModel(
    override val text: Int,
    override val leadingIcon: Int,
    override val trailingType: BSTrailingType,
    override val onClick: () -> Unit = {},
): BSListModel

/**
 * The Settings Bottom Sheet component.
 * * It provides a list of toggleable preferences and navigation links to sub-menus
 * (like Quality selection). The list is memoized using [remember] to ensure
 * the UI only updates when the underlying [playerSettings] change.
 *
 * @param playerSettings The current user preferences used to populate toggle states.
 * @param onPlayerIntent Callback to dispatch user actions back to the ViewModel.
 */
@Composable
internal fun SettingsBS(
    playerSettings: PlayerSettings,
    onPlayerIntent: (PlayerIntent) -> Unit
) {
    val settings = remember(playerSettings) {
        listOf(
            SettingItemModel(
                text = R.string.quality_label,
                leadingIcon = LibertyFlowIcons.Outlined.HighQuality,
                trailingType = BSTrailingType.Navigation,
                onClick = { onPlayerIntent(PlayerIntent.ToggleQualityBS) }
            ),
            SettingItemModel(
                leadingIcon = LibertyFlowIcons.Outlined.RewindForwardCircle,
                text = R.string.skip_opening_buttons_label,
                trailingType = BSTrailingType.Toggle(playerSettings.showSkipOpeningButton),
                onClick = { onPlayerIntent(PlayerIntent.ToggleShowSkipOpeningButton) }
            ),
            SettingItemModel(
                leadingIcon = LibertyFlowIcons.Outlined.Rocket,
                text = R.string.auto_skip_opening_label,
                trailingType = BSTrailingType.Toggle(playerSettings.autoSkipOpening),
                onClick = { onPlayerIntent(PlayerIntent.ToggleAutoSkipOpening) }
            ),
            SettingItemModel(
                leadingIcon = LibertyFlowIcons.Outlined.PlayCircle,
                text = R.string.autoplay_label,
                trailingType = BSTrailingType.Toggle(playerSettings.autoPlay),
                onClick = { onPlayerIntent(PlayerIntent.ToggleAutoPlay) }
            ),
        )
    }

    BSList(
        items = settings,
        onDismissRequest = { onPlayerIntent(PlayerIntent.ToggleSettingsBS) }
    )
}