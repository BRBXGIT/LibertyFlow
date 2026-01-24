@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.design_system.components.bottom_sheets.player_settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.player.VideoQuality
import com.example.design_system.theme.LibertyFlowIcons
import com.example.design_system.theme.mShapes

@Composable
fun VideoQualityBS(
    onDismissRequest: () -> Unit,
    onQualitySelect: (VideoQuality) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
    ) {
        Text(
            text = "Выберите качество видео",
            modifier = Modifier.padding(16.dp),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        HorizontalDivider()

        LazyColumn(
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            items(VideoQuality.entries) { quality ->
                ListItem(
                    colors = ListItemDefaults.colors(
                        containerColor = Color.Transparent
                    ),
                    headlineContent = {
                        Text(text = quality.name)
                    },
                    supportingContent = {
                        Text(text = getQualityDescription(quality))
                    },
                    trailingContent = {
                        Icon(
                            painter = painterResource(LibertyFlowIcons.Filters),
                            contentDescription = null
                        )
                    },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clip(mShapes.small)
                        .clickable {
                            onQualitySelect(quality)
                            onDismissRequest()
                        }
                )
            }
        }
    }
}

private fun getQualityDescription(quality: VideoQuality): String {
    return when (quality) {
        VideoQuality.SD -> "480p - Экономия трафика"
        VideoQuality.HD -> "720p - Стандартное качество"
        VideoQuality.FHD -> "1080p - Высокое качество"
    }
}