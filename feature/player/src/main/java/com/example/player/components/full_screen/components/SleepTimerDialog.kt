package com.example.player.components.full_screen.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.design_system.components.dialogs.dialog_list.DialogList
import com.example.design_system.components.list_tems.LibertyFlowListItemModel
import com.example.design_system.components.list_tems.LibertyFlowListItemTrailingType
import com.example.player.R
import com.example.player.player.PlayerIntent

/**
 * A private implementation of [LibertyFlowListItemModel] for sleep timer durations.
 *
 * @property text The string resource ID for the time duration label (e.g., "10 minutes").
 * @property leadingIcon Optional icon; defaults to null for a cleaner timer list.
 * @property trailingType Shows a Toggle if the specific time is currently active, else Navigation.
 * @property onClick The action to set the sleep timer duration.
 * @property time The raw integer value in minutes representing this selection.
 */
private data class SleepItemModel(
    @param:StringRes override val text: Int,
    @param:DrawableRes override val leadingIcon: Int? = null,
    override val trailingType: LibertyFlowListItemTrailingType,
    override val onClick: () -> Unit = {},

    val time: Int
): LibertyFlowListItemModel

private enum class SleepTime { TEN, TWENTY, THIRTY, FORTY, FIFTY, SIXTY }

/**
 * A selection dialog for configuring a playback sleep timer.
 * * Maps [SleepTime] presets into a list of selectable items. If [currentSleepTime]
 * matches one of the presets, that item is marked as active.
 *
 * @param currentSleepTime The currently active timer value in minutes, if any.
 * @param onPlayerIntent The intent handler to set the timer or toggle the dialog visibility.
 */
@Composable
internal fun SleepTimerDialog(
    currentSleepTime: Int? = null,
    onPlayerIntent: (PlayerIntent) -> Unit
) {
    val items = remember(currentSleepTime) {
        SleepTime.entries.map { time ->
            SleepItemModel(
                text = time.toSleepTimeLabelRes(),
                trailingType = if (time.toMinutes() == currentSleepTime) {
                    LibertyFlowListItemTrailingType.Toggle(isEnabled = true)
                } else {
                    LibertyFlowListItemTrailingType.Navigation
                },
                onClick = { onPlayerIntent(PlayerIntent.SetSleepTimer(1)) },
                time = time.toMinutes()
            )
        }
    }

    DialogList(
        items = items,
        onDismissRequest = { onPlayerIntent(PlayerIntent.ToggleSleepTimeDialog) }
    )
}

private fun SleepTime.toMinutes() = when(this) {
    SleepTime.TEN -> 10
    SleepTime.TWENTY -> 20
    SleepTime.THIRTY -> 30
    SleepTime.FORTY -> 40
    SleepTime.FIFTY -> 50
    SleepTime.SIXTY -> 60
}

private val TenMinutesLabelRes = R.string.ten_minutes
private val TwentyMinutesLabelRes = R.string.twenty_minutes
private val ThirtyMinutesLabelRes = R.string.thirty_minutes
private val FortyMinutesLabelRes = R.string.forty_minutes
private val FiftyMinutesLabelRes = R.string.fifty_minutes
private val SixtyMinutesLabelRes = R.string.sixty_minutes

private fun SleepTime.toSleepTimeLabelRes() = when(this) {
    SleepTime.TEN -> TenMinutesLabelRes
    SleepTime.TWENTY -> TwentyMinutesLabelRes
    SleepTime.THIRTY -> ThirtyMinutesLabelRes
    SleepTime.FORTY -> FortyMinutesLabelRes
    SleepTime.FIFTY -> FiftyMinutesLabelRes
    SleepTime.SIXTY -> SixtyMinutesLabelRes
}