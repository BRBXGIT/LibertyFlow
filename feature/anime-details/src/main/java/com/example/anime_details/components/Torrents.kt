package com.example.anime_details.components

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.example.common.ui_helpers.effects.UiEffect
import com.example.data.models.releases.anime_details.UiTorrent
import com.example.design_system.theme.LibertyFlowIcons
import com.example.design_system.theme.mColors
import com.example.design_system.theme.mTypography

private const val HORIZONTAL_PADDING = 16
private const val VERTICAL_SPACING = 8

private const val BASE_TORRENTS_URL = "https://static.wwnd.space"

/**
 * Displays a torrent row with title, quality, metadata and download action.
 */
@Composable
internal fun LazyItemScope.Torrent(
    torrent: UiTorrent,
    onEffect: (UiEffect) -> Unit
) {
    Row(
        modifier = Modifier
            .animateItem()
            .fillMaxWidth()
            .padding(horizontal = HORIZONTAL_PADDING.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Left side: title + torrent info
        Column(
            verticalArrangement = Arrangement.spacedBy(VERTICAL_SPACING.dp)
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
            Icon(
                painter = painterResource(LibertyFlowIcons.Download),
                contentDescription = null
            )
        }
    }
}

private const val TORRENT_INFO_SPACING = 4
private const val MEGABYTES_SUFFIX = "mb"
private const val INFO_ICON_SIZE = 16

/**
 * Displays torrent metadata: size, seeders and leechers.
 */
@Composable
private fun TorrentInfo(
    weight: Int,
    seeders: Int,
    leechers: Int
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(TORRENT_INFO_SPACING.dp)
    ) {
        Text(
            text = "$weight$MEGABYTES_SUFFIX",
            style = mTypography.bodyMedium,
            color = mColors.primary
        )

        InfoIcon(
            icon = LibertyFlowIcons.DoubleArrowUp,
            tint = Color.Green
        )
        Text(seeders.toString(), style = mTypography.bodyMedium)

        InfoIcon(
            icon = LibertyFlowIcons.DoubleArrowDown,
            tint = Color.Red
        )
        Text(leechers.toString(), style = mTypography.bodyMedium)
    }
}

/**
 * Reusable small icon for torrent info row.
 */
@Composable
private fun InfoIcon(
    icon: Int,
    tint: Color
) {
    Icon(
        painter = painterResource(icon),
        contentDescription = null,
        tint = tint,
        modifier = Modifier.size(INFO_ICON_SIZE.dp)
    )
}