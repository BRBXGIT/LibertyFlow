@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.player.components.full_screen.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.data.models.player.PlayerSettings
import com.example.design_system.components.dialogs.dialog_list.DialogList
import com.example.design_system.components.list_tems.LibertyFlowListItemModel
import com.example.design_system.components.list_tems.LibertyFlowListItemTrailingType
import com.example.design_system.theme.icons.LibertyFlowIcons
import com.example.player.R
import com.example.player.player.PlayerIntent

/**
 * A private data implementation of [LibertyFlowListItemModel] tailored for the Settings menu.
 * @property text The string resource ID for the setting's title.
 * @property leadingIcon The mandatory drawable resource ID representing the setting's category.
 * @property trailingType The visual indicator (Toggle state or Navigation arrow).
 * @property onClick The action sent to the ViewModel/Reducer when the setting is interacted with.
 */
private data class SettingItemModel(
    override val text: Int,
    override val leadingIcon: Int,
    override val trailingType: LibertyFlowListItemTrailingType,
    override val onClick: () -> Unit = {},
): LibertyFlowListItemModel

/**
 * A specialized dialog for managing playback configurations.
 * * This Composable observes the [playerSettings] state and transforms it into a
 * list of interactive items. Every interaction is wrapped into a [PlayerIntent]
 * and passed up to the caller, ensuring a clean separation between UI and business logic.
 *
 * @param playerSettings The current state of the player (quality, autoplay, etc.).
 * @param onPlayerIntent The single entry point for all user actions within this dialog.
 */
@Composable
internal fun SettingsDialog(
    playerSettings: PlayerSettings,
    onPlayerIntent: (PlayerIntent) -> Unit
) {
    val settings = remember(playerSettings) {
        listOf(
            SettingItemModel(
                text = R.string.quality_label,
                leadingIcon = LibertyFlowIcons.Outlined.HighQuality,
                trailingType = LibertyFlowListItemTrailingType.Navigation,
                onClick = { onPlayerIntent(PlayerIntent.ToggleQualityDialog) }
            ),
            SettingItemModel(
                leadingIcon = LibertyFlowIcons.Outlined.RewindForwardCircle,
                text = R.string.skip_opening_buttons_label,
                trailingType = LibertyFlowListItemTrailingType.Toggle(playerSettings.showSkipOpeningButton),
                onClick = { onPlayerIntent(PlayerIntent.ToggleShowSkipOpeningButton) }
            ),
            SettingItemModel(
                leadingIcon = LibertyFlowIcons.Outlined.Rocket,
                text = R.string.auto_skip_opening_label,
                trailingType = LibertyFlowListItemTrailingType.Toggle(playerSettings.autoSkipOpening),
                onClick = { onPlayerIntent(PlayerIntent.ToggleAutoSkipOpening) }
            ),
            SettingItemModel(
                leadingIcon = LibertyFlowIcons.Outlined.PlayCircle,
                text = R.string.autoplay_label,
                trailingType = LibertyFlowListItemTrailingType.Toggle(playerSettings.autoPlay),
                onClick = { onPlayerIntent(PlayerIntent.ToggleAutoPlay) }
            ),
        )
    }

    DialogList(
        items = settings,
        onDismissRequest = { onPlayerIntent(PlayerIntent.ToggleSettingsDialog) }
    )
}