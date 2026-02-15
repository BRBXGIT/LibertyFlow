@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.design_system.components.bottom_sheets.quality_bs

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.data.models.player.VideoQuality
import com.example.design_system.R
import com.example.design_system.components.bottom_sheets.bs_list.BSList
import com.example.design_system.components.bottom_sheets.bs_list.BSListModel
import com.example.design_system.components.bottom_sheets.bs_list.BSTrailingType

/**
 * An implementation of [BSListModel] for the Video Quality selection list.
 * * Uses explicit [StringRes] and [DrawableRes] annotations to ensure the
 * correct resource types are passed during construction.
 *
 * @property text The string resource for the quality label (e.g., "HD 720p").
 * @property leadingIcon Optional icon for the quality level.
 * @property onClick Action to trigger when a quality level is selected.
 * @property trailingType The visual indicator (Checkmark if selected, Chevron if not).
 */
private data class QualityItem(
    @param:StringRes override val text: Int,
    @param:DrawableRes override val leadingIcon: Int? = null,
    override val onClick: () -> Unit,
    override val trailingType: BSTrailingType,
): BSListModel

/**
 * A Bottom Sheet that allows users to switch between different video resolutions.
 *
 * This component utilizes [BSList] to render the options defined in the
 * [VideoQuality] enum. The [selectedQuality] is highlighted with a checkmark.
 *
 * @param onItemClick Callback triggered with the chosen [VideoQuality] when an item is tapped.
 * @param selectedQuality The current active quality level used for state highlighting.
 * @param onDismissRequest Callback to close the bottom sheet.
 */
@Composable
fun VideoQualityBS(
    onItemClick: (VideoQuality) -> Unit,
    selectedQuality: VideoQuality,
    onDismissRequest: () -> Unit
) {
    val items = remember(selectedQuality) {
        VideoQuality.entries.map { quality ->
            QualityItem(
                text = quality.toLabelRes(),
                trailingType = if (quality == selectedQuality) {
                    BSTrailingType.Toggle(isEnabled = true)
                } else {
                    BSTrailingType.Navigation
                },
                onClick = { onItemClick(quality) }
            )
        }
    }

    BSList(
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