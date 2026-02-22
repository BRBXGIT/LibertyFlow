package com.example.player.components.full_screen.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.data.models.player.VideoQuality
import com.example.design_system.R
import com.example.design_system.components.dialogs.dialog_list.DialogList
import com.example.design_system.components.list_tems.LibertyFlowListItemModel
import com.example.design_system.components.list_tems.LibertyFlowListItemTrailingType

/**
 * A private data implementation of [LibertyFlowListItemModel] specifically for video resolution options.
 * @property text The string resource ID for the quality label (e.g., "SD", "HD").
 * @property leadingIcon Optional icon for the quality level (defaults to null).
 * @property onClick The action to trigger when this quality level is selected.
 * @property trailingType The visual indicator (Toggle checkmark if selected, Navigation arrow otherwise).
 */
private data class QualityItem(
    @param:StringRes override val text: Int,
    @param:DrawableRes override val leadingIcon: Int? = null,
    override val onClick: () -> Unit,
    override val trailingType: LibertyFlowListItemTrailingType,
): LibertyFlowListItemModel

/**
 * A selection dialog that allows the user to choose a specific [VideoQuality].
 * * This Composable maps all available [VideoQuality] entries into a list of items,
 * highlighting the [selectedQuality] with an active toggle state.
 *
 * @param onItemClick Callback invoked with the specific [VideoQuality] chosen by the user.
 * @param selectedQuality The currently active video quality used to drive the UI selection state.
 * @param onDismissRequest Callback to close the dialog.
 */
@Composable
fun VideoQualityDialog(
    onItemClick: (VideoQuality) -> Unit,
    selectedQuality: VideoQuality,
    onDismissRequest: () -> Unit
) {
    val items = remember(selectedQuality) {
        VideoQuality.entries.map { quality ->
            QualityItem(
                text = quality.toLabelRes(),
                trailingType = if (quality == selectedQuality) {
                    LibertyFlowListItemTrailingType.Toggle(isEnabled = true)
                } else {
                    LibertyFlowListItemTrailingType.Navigation
                },
                onClick = { onItemClick(quality) }
            )
        }
    }

    DialogList(
        onDismissRequest = onDismissRequest,
        items = items
    )
}

private fun VideoQuality.toLabelRes(): Int {
    return when(this) {
        VideoQuality.SD -> R.string.sd_label
        VideoQuality.HD -> R.string.hd_label
        VideoQuality.FHD -> R.string.fhd_label
    }
}