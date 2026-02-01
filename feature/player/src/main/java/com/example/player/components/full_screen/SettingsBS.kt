@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.player.components.full_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.data.models.player.PlayerSettings
import com.example.design_system.components.bottom_sheets.bs_list_item.BSListItem
import com.example.design_system.theme.icons.LibertyFlowIcons
import com.example.design_system.theme.theme.mColors
import com.example.design_system.theme.theme.mShapes
import com.example.player.R
import com.example.player.player.PlayerIntent

private val SHEET_CONTENT_PADDING = 16.dp
private val LIST_SPACING = 8.dp

private sealed interface TrailingType {
    data object Navigation: TrailingType
    data class Toggle(val isEnabled: Boolean): TrailingType

    @Composable
    fun getIcon() = when (this) {
        is Navigation -> LibertyFlowIcons.ArrowRightCircle
        is Toggle -> if (isEnabled) LibertyFlowIcons.CheckCircle else LibertyFlowIcons.CrossCircle
    }

    @Composable
    fun getColor() = when (this) {
        is Navigation -> mColors.onSurface
        is Toggle -> if (isEnabled) mColors.primary else mColors.error
    }
}

private data class SettingItemModel(
    val leadingIcon: Int,
    val label: Int,
    val intent: PlayerIntent,
    val trailingType: TrailingType
)

@Composable
internal fun SettingsBS(
    playerSettings: PlayerSettings,
    onPlayerIntent: (PlayerIntent) -> Unit
) {
    val settings = remember(playerSettings) {
        listOf(
            SettingItemModel(
                leadingIcon = LibertyFlowIcons.HighQuality,
                label = R.string.quality_label,
                intent = PlayerIntent.ToggleQualityBS,
                trailingType = TrailingType.Navigation
            ),
            SettingItemModel(
                leadingIcon = LibertyFlowIcons.RewindForwardCircle,
                label = R.string.skip_opening_buttons_label,
                intent = PlayerIntent.ToggleShowSkipOpeningButton,
                trailingType = TrailingType.Toggle(playerSettings.showSkipOpeningButton)
            ),
            SettingItemModel(
                leadingIcon = LibertyFlowIcons.Rocket,
                label = R.string.auto_skip_opening_label,
                intent = PlayerIntent.ToggleAutoSkipOpening,
                trailingType = TrailingType.Toggle(playerSettings.autoSkipOpening)
            ),
            SettingItemModel(
                leadingIcon = LibertyFlowIcons.PlayCircle,
                label = R.string.autoplay_label,
                intent = PlayerIntent.ToggleAutoPlay,
                trailingType = TrailingType.Toggle(playerSettings.autoPlay)
            )
        )
    }

    ModalBottomSheet(
        shape = mShapes.small,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        onDismissRequest = { onPlayerIntent(PlayerIntent.ToggleSettingsBS) }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(LIST_SPACING),
            contentPadding = PaddingValues(SHEET_CONTENT_PADDING)
        ) {
            items(
                items = settings,
                key = { it.label }
            ) { setting ->
                BSListItem(
                    leadingIcon = setting.leadingIcon,
                    label = stringResource(setting.label),
                    trailingIcon = setting.trailingType.getIcon(),
                    trailingIconColor = setting.trailingType.getColor(),
                    onClick = {
                        onPlayerIntent(setting.intent)
                        onPlayerIntent(PlayerIntent.ToggleSettingsBS)
                    }
                )
            }
        }
    }
}