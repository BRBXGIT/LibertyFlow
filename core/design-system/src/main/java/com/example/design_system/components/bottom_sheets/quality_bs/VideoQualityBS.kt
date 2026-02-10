@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.design_system.components.bottom_sheets.quality_bs

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.data.models.player.VideoQuality
import com.example.design_system.R
import com.example.design_system.components.bottom_sheets.bs_list.BSList
import com.example.design_system.components.bottom_sheets.bs_list.BSListModel
import com.example.design_system.components.bottom_sheets.bs_list.BSTrailingType

private data class QualityItem(
    override val label: Int,
    override val leadingIcon: Int? = null,
    override val onClick: () -> Unit,
    override val trailingType: BSTrailingType,

    val quality: VideoQuality
): BSListModel

@Composable
fun VideoQualityBS(
    onItemClick: (VideoQuality) -> Unit,
    selectedQuality: VideoQuality,
    onDismissRequest: () -> Unit
) {
    val items = remember(selectedQuality) {
        VideoQuality.entries.map { quality ->
            QualityItem(
                label = quality.toLabelRes(),
                quality = quality,
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