@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.design_system.components.bottom_sheets.player_settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.data.models.player.VideoQuality
import com.example.design_system.components.bottom_sheets.bs_list_item.BSListItem
import com.example.design_system.theme.LibertyFlowIcons
import com.example.design_system.theme.mColors
import com.example.design_system.theme.mShapes

private val LIST_SPACING = 8.dp
private val SHEET_CONTENT_PADDING = 16.dp

@Composable
fun VideoQualityBS(
    onItemClick: (VideoQuality) -> Unit,
    selectedQuality: VideoQuality,
    onDismissRequest: () -> Unit
) {
    ModalBottomSheet(
        shape = mShapes.small,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        onDismissRequest = onDismissRequest
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(LIST_SPACING),
            contentPadding = PaddingValues(SHEET_CONTENT_PADDING)
        ) {
            items(
                items = VideoQuality.entries,
                key = { it }
            ) { quality ->
                BSListItem(
                    label = "${quality.name} ${quality.toLabel()}",
                    trailingIcon = if (quality == selectedQuality) LibertyFlowIcons.CheckCircle else LibertyFlowIcons.ArrowRightCircle,
                    trailingIconColor = if (quality == selectedQuality) mColors.primary else mColors.onSurface,
                    onClick = {
                        onItemClick(quality)
                        onDismissRequest()
                    }
                )
            }
        }
    }
}

private const val pPostfix = "p"
private const val SD = "480"
private const val HD = "720"
private const val FHD = "1080"

private fun VideoQuality.toLabel(): String {
    return when(this) {
        VideoQuality.SD -> SD + pPostfix
        VideoQuality.HD -> HD + pPostfix
        VideoQuality.FHD -> FHD + pPostfix
    }
}