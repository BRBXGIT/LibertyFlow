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
import com.example.data.models.player.VideoQuality
import com.example.design_system.theme.LibertyFlowIcons
import com.example.design_system.theme.mColors
import com.example.design_system.theme.mShapes
import com.example.player.R
import com.example.player.player.PlayerIntent

private sealed interface SettingType {
    data class Quality(val quality: VideoQuality): SettingType
    data class OpeningButton(val show: Boolean): SettingType
    data class AutoSkipOpening(val skip: Boolean): SettingType
    data class AutoPlay(val autoPlay: Boolean): SettingType
}

private data class Setting(
    val icon: Int,
    val label: Int,
    val intent: PlayerIntent,
    val type: SettingType,
    val isEnabled: Boolean? = null
)

private val QualityLabel = R.string.quality_label
private val SkipOpeningButtonsLabel = R.string.skip_opening_buttons_label
private val AutoSkipOpeningLabel = R.string.auto_skip_opening_label
private val AutoplayLabel = R.string.autoplay_label

@Composable
internal fun SettingsBS(
    playerSettings: PlayerSettings,
    onPlayerIntent: (PlayerIntent) -> Unit
) {
    val settings = remember {
        listOf(
            Setting(
                icon = LibertyFlowIcons.HighQuality,
                label = QualityLabel,
                intent = PlayerIntent.ToggleSettingsBS,
                type = SettingType.Quality(playerSettings.quality)
            ),
            Setting(
                icon = LibertyFlowIcons.RewindForwardCircle,
                label = SkipOpeningButtonsLabel,
                intent = PlayerIntent.ToggleShowSkipOpeningButton,
                type = SettingType.OpeningButton(playerSettings.showSkipOpeningButton),
                isEnabled = playerSettings.showSkipOpeningButton
            ),
            Setting(
                icon = LibertyFlowIcons.Rocket,
                label = AutoSkipOpeningLabel,
                intent = PlayerIntent.ToggleAutoSkipOpening,
                type = SettingType.AutoSkipOpening(playerSettings.autoSkipOpening),
                isEnabled = playerSettings.autoSkipOpening
            ),
            Setting(
                icon = LibertyFlowIcons.PlayCircle,
                label = AutoplayLabel,
                intent = PlayerIntent.ToggleAutoPlay,
                type = SettingType.AutoPlay(playerSettings.autoPlay),
                isEnabled = playerSettings.autoPlay
            )
        )
    }

    ModalBottomSheet(
        shape = mShapes.small,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        onDismissRequest = { onPlayerIntent(PlayerIntent.ToggleSettingsBS) }
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(settings) { setting ->
                SettingsItem(setting, onPlayerIntent)
            }
        }
    }
}

private val ICON_LABEL_ARRANGEMENT = 16.dp

private const val LABEL_MAX_LINES = 1

private const val WEIGHT = 1f

@Composable
private fun SettingsItem(
    setting: Setting,
    onPlayerIntent: (PlayerIntent) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(mShapes.small)
            .clickable { onPlayerIntent(setting.intent) }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(WEIGHT),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ICON_LABEL_ARRANGEMENT)
        ) {
            Icon(
                painter = painterResource(setting.icon),
                contentDescription = null
            )

            Text(
                text = stringResource(setting.label),
                overflow = TextOverflow.Ellipsis,
                maxLines = LABEL_MAX_LINES
            )
        }

        if (setting.type is SettingType.Quality) {
            Icon(
                painter = painterResource(LibertyFlowIcons.ArrowRightCircle),
                contentDescription = null
            )
        } else {
            Icon(
                tint = if (setting.isEnabled!!) mColors.primary else mColors.error,
                contentDescription = null,
                painter = painterResource(
                    id = if (setting.isEnabled) {
                        LibertyFlowIcons.CheckCircle
                    } else LibertyFlowIcons.CrossCircle),
            )
        }
    }
}