package com.example.anime_details.components

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.example.common.ui_helpers.effects.UiEffect
import com.example.data.models.releases.anime_details.Torrent
import com.example.design_system.components.icons.LibertyFlowIcon
import com.example.design_system.theme.icons.LibertyFlowIcons
import com.example.design_system.theme.theme.LibertyFlowTheme
import com.example.design_system.theme.theme.mColors
import com.example.design_system.theme.theme.mDimens
import com.example.design_system.theme.theme.mTypography

private const val BASE_TORRENTS_URL = "https://static.wwnd.space"

/**
 * A list item representing a downloadable torrent for an anime episode or pack.
 *
 * * **Layout:** Uses a [Row] with a space-between arrangement. The left side contains
 * the file name and metadata, while the right side hosts the download action.
 * * **Interaction:** Tapping the download button creates an [Intent.ACTION_VIEW]
 * with a magnet URI, dispatched via [UiEffect.IntentTo].
 *
 * @param torrent The torrent data model containing file names, seeders, and magnet link.
 * @param onEffect Callback used to trigger external navigation or system intents.
 */
@Composable
internal fun LazyItemScope.Torrent(
    torrent: Torrent,
    onEffect: (UiEffect) -> Unit
) {
    Row(
        modifier = Modifier
            .animateItem()
            .fillMaxWidth()
            .padding(horizontal = mDimens.paddingMedium),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Left side: title + torrent info
        Column(
            verticalArrangement = Arrangement.spacedBy(mDimens.spacingSmall)
        ) {
            Text(
                text = torrent.croppedFileName(),
                style = mTypography.bodyLarge
            )

            TorrentInfo(
                weight = torrent.size,
                seeders = torrent.seeders,
                leechers = torrent.leechers
            )
        }

        // Right side: download action
        val intent =
            Intent(
                Intent.ACTION_VIEW,
                (BASE_TORRENTS_URL + torrent.magnet).toUri()
            )
        IconButton(
            onClick = { onEffect(UiEffect.IntentTo(intent)) }
        ) {
            LibertyFlowIcon(
                LibertyFlowIcons.Filled.Download
            )
        }
    }
}

private const val MEGABYTES_SUFFIX = "mb"

/**
 * Displays key torrent health metrics: file size, seeders (uploaders), and leechers (downloaders).
 *
 * @param weight Total file size in Megabytes.
 * @param seeders Count of active peers sharing the full file.
 * @param leechers Count of active peers downloading the file.
 */
@Composable
private fun TorrentInfo(
    weight: Int,
    seeders: Int,
    leechers: Int
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(mDimens.spacingExtraSmall)
    ) {
        Text(
            text = "$weight$MEGABYTES_SUFFIX",
            style = mTypography.bodyMedium,
            color = mColors.primary
        )

        InfoIcon(
            icon = LibertyFlowIcons.Filled.DoubleArrowUp,
            tint = Color.Green
        )
        Text(seeders.toString(), style = mTypography.bodyMedium)

        InfoIcon(
            icon = LibertyFlowIcons.Filled.DoubleArrowDown,
            tint = Color.Red
        )
        Text(leechers.toString(), style = mTypography.bodyMedium)
    }
}

private const val INFO_ICON_SIZE = 16

/**
 * Reusable small icon for torrent info row.
 */
@Composable
private fun InfoIcon(
    icon: Int,
    tint: Color
) {
    LibertyFlowIcon(
        icon = icon,
        tint = tint,
        modifier = Modifier.size(INFO_ICON_SIZE.dp)
    )
}

@Preview
@Composable
private fun TorrentPreview() {
    LibertyFlowTheme {
        LazyColumn {
            item {
                Torrent(
                    torrent = Torrent(),
                    onEffect = {}
                )
            }
        }
    }
}