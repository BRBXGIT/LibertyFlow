@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.player.components.full_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.data.models.player.PlayerSettings
import com.example.design_system.theme.LibertyFlowIcons
import com.example.design_system.theme.mColors
import com.example.design_system.theme.mShapes
import com.example.design_system.theme.mTypography
import com.example.player.R
import com.example.player.player.PlayerIntent

private val ICON_LABEL_GAP = 16.dp
private val ITEM_PADDING = 12.dp
private val SHEET_CONTENT_PADDING = 16.dp
private val LIST_SPACING = 8.dp
private const val LABEL_MAX_LINES = 1
private const val WEIGHT_FILL = 1f

private val QualityLabel = R.string.quality_label
private val SkipOpeningButtonsLabel = R.string.skip_opening_buttons_label
private val AutoSkipOpeningLabel = R.string.auto_skip_opening_label
private val AutoplayLabel = R.string.autoplay_label

private sealed interface TrailingType {
    data object Navigation: TrailingType
    data class Toggle(val isEnabled: Boolean): TrailingType
}

private data class SettingItemModel(
    val icon: Int,
    val label: Int,
    val intent: PlayerIntent,
    val trailing: TrailingType
)

@Composable
internal fun SettingsBS(
    playerSettings: PlayerSettings,
    onPlayerIntent: (PlayerIntent) -> Unit
) {
    val settings = remember(playerSettings) {
        listOf(
            SettingItemModel(
                icon = LibertyFlowIcons.HighQuality,
                label = QualityLabel,
                intent = PlayerIntent.ToggleSettingsBS,
                trailing = TrailingType.Navigation
            ),
            SettingItemModel(
                icon = LibertyFlowIcons.RewindForwardCircle,
                label = SkipOpeningButtonsLabel,
                intent = PlayerIntent.ToggleShowSkipOpeningButton,
                trailing = TrailingType.Toggle(playerSettings.showSkipOpeningButton)
            ),
            SettingItemModel(
                icon = LibertyFlowIcons.Rocket,
                label = AutoSkipOpeningLabel,
                intent = PlayerIntent.ToggleAutoSkipOpening,
                trailing = TrailingType.Toggle(playerSettings.autoSkipOpening)
            ),
            SettingItemModel(
                icon = LibertyFlowIcons.PlayCircle,
                label = AutoplayLabel,
                intent = PlayerIntent.ToggleAutoPlay,
                trailing = TrailingType.Toggle(playerSettings.autoPlay)
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
                SettingsRow(
                    setting = setting,
                    onClick = {
                        onPlayerIntent(setting.intent)
                        onPlayerIntent(PlayerIntent.ToggleSettingsBS)
                    }
                )
            }
        }
    }
}

@Composable
private fun SettingsRow(
    setting: SettingItemModel,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(mShapes.small)
            .clickable(onClick = onClick)
            .padding(ITEM_PADDING),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(ICON_LABEL_GAP)
    ) {
        Icon(
            painter = painterResource(setting.icon),
            contentDescription = null,
            tint = mColors.onSurface
        )

        Text(
            modifier = Modifier.weight(WEIGHT_FILL),
            text = stringResource(setting.label),
            overflow = TextOverflow.Ellipsis,
            maxLines = LABEL_MAX_LINES,
            style = mTypography.bodyLarge
        )

        SettingsTrailingIcon(setting.trailing)
    }
}

@Composable
private fun SettingsTrailingIcon(type: TrailingType) {
    when (type) {
        is TrailingType.Navigation -> {
            Icon(
                painter = painterResource(LibertyFlowIcons.ArrowRightCircle),
                contentDescription = null,
                tint = mColors.onSurfaceVariant
            )
        }
        is TrailingType.Toggle -> {
            val iconRes = if (type.isEnabled) {
                LibertyFlowIcons.CheckCircle
            } else {
                LibertyFlowIcons.CrossCircle
            }

            val iconColor = if (type.isEnabled) mColors.primary else mColors.error

            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = iconColor
            )
        }
    }
}