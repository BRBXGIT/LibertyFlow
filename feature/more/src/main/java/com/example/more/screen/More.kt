package com.example.more.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.design_system.components.bars.bottom_nav_bar.calculateNavBarSize
import com.example.design_system.components.dividers.dividerWithLabel
import com.example.design_system.theme.LibertyFlowIcons
import com.example.design_system.theme.mColors
import com.example.more.R
import com.example.more.components.MoreItem
import com.example.more.components.TopBar

private const val LC_SPACED_BY = 16
private const val LC_VERTICAL_PADDING = 16

private val LINKS_LABEL = R.string.links_label

@Composable
internal fun More() {
    Scaffold(
        topBar = { TopBar() },
        contentWindowInsets = WindowInsets(bottom = calculateNavBarSize()),
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(mColors.background)
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = calculateNavBarSize()
                )
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(LC_SPACED_BY.dp),
                contentPadding = PaddingValues(vertical = LC_VERTICAL_PADDING.dp)
            ) {
                appItems()

                dividerWithLabel(LINKS_LABEL)

                linkItems()
            }
        }
    }
}

private val InfoLabel = R.string.info_label
private val SettingsLabel = R.string.settings_label

private val appItems = listOf(
    MoreItem(
        icon = LibertyFlowIcons.Settings,
        labelRes = SettingsLabel,
        originalColor = false,
        onClick = { /* TODO: handle click */ }
    ),
    MoreItem(
        icon = LibertyFlowIcons.Info,
        labelRes = InfoLabel,
        originalColor = false,
        onClick = { /* TODO: handle click */ }
    )
)

private fun LazyListScope.appItems() {
    items(
        items = appItems,
        key = { item -> item.labelRes }
    ) { item ->
        MoreItem(item)
    }
}

private val VKLabel = R.string.vk_label
private val YouTubeLabel = R.string.youtube_label
private val PatreonLabel = R.string.patreon_label
private val TelegramLabel = R.string.telegram_label
private val DiscordLabel = R.string.discord_label
private val AniLibertyLabel = R.string.aniliberty_label

private val linkItems = listOf(
    MoreItem(
        icon = LibertyFlowIcons.VK,
        labelRes = VKLabel,
        onClick = { /* TODO: handle click */ }
    ),
    MoreItem(
        icon = LibertyFlowIcons.YouTube,
        labelRes = YouTubeLabel,
        onClick = { /* TODO: handle click */ }
    ),
    MoreItem(
        icon = LibertyFlowIcons.Patreon,
        labelRes = PatreonLabel,
        onClick = { /* TODO: handle click */ }
    ),
    MoreItem(
        icon = LibertyFlowIcons.Telegram,
        labelRes = TelegramLabel,
        onClick = { /* TODO: handle click */ }
    ),
    MoreItem(
        icon = LibertyFlowIcons.Discord,
        labelRes = DiscordLabel,
        onClick = { /* TODO: handle click */ }
    ),
    MoreItem(
        icon = LibertyFlowIcons.AniLiberty,
        labelRes = AniLibertyLabel,
        onClick = { /* TODO: handle click */ }
    ),
)

private fun LazyListScope.linkItems() {
    items(
        items = linkItems,
        key = { item -> item.labelRes }
    ) { item ->
        MoreItem(item)
    }
}