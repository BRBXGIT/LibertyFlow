package com.example.more.screen

import android.content.Intent
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
import androidx.core.net.toUri
import com.example.common.ui_helpers.UiEffect
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
internal fun More(
    onEffect: (UiEffect) -> Unit
) {
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
                appItems(onEffect)

                dividerWithLabel(LINKS_LABEL)

                linkItems(onEffect)
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
        effect = UiEffect.NavigateBack // TODO: Add screen
    ),
    MoreItem(
        icon = LibertyFlowIcons.Info,
        labelRes = InfoLabel,
        originalColor = false,
        effect = UiEffect.NavigateBack // TODO: Add screen
    )
)

private fun LazyListScope.appItems(onEffect: (UiEffect) -> Unit) {
    items(
        items = appItems,
        key = { item -> item.labelRes }
    ) { item ->
        MoreItem(item, onEffect)
    }
}

private val VKLabel = R.string.vk_label
private val YouTubeLabel = R.string.youtube_label
private val PatreonLabel = R.string.patreon_label
private val TelegramLabel = R.string.telegram_label
private val DiscordLabel = R.string.discord_label
private val AniLibertyLabel = R.string.aniliberty_label

private const val VKLink = "https://vk.com/anilibria"
private const val YouTubeLink = "https://www.youtube.com/@anilibriatv"
private const val PatreonLink = "https://patreon.com/anilibria"
private const val TelegramLink = "https://t.me/anilibria_tv"
private const val DiscordLink = "https://discord.gg/M6yCGeGN9B"
private const val AniLibertyLink = "https://aniliberty.top"

private val linkItems = listOf(
    MoreItem(
        icon = LibertyFlowIcons.VK,
        labelRes = VKLabel,
        effect = UiEffect.IntentTo(
            Intent(
                Intent.ACTION_VIEW,
                VKLink.toUri()
            )
        )
    ),
    MoreItem(
        icon = LibertyFlowIcons.YouTube,
        labelRes = YouTubeLabel,
        effect = UiEffect.IntentTo(
            Intent(
                Intent.ACTION_VIEW,
                YouTubeLink.toUri()
            )
        )
    ),
    MoreItem(
        icon = LibertyFlowIcons.Patreon,
        labelRes = PatreonLabel,
        effect = UiEffect.IntentTo(
            Intent(
                Intent.ACTION_VIEW,
                PatreonLink.toUri()
            )
        )
    ),
    MoreItem(
        icon = LibertyFlowIcons.Telegram,
        labelRes = TelegramLabel,
        effect = UiEffect.IntentTo(
            Intent(
                Intent.ACTION_VIEW,
                TelegramLink.toUri()
            )
        )
    ),
    MoreItem(
        icon = LibertyFlowIcons.Discord,
        labelRes = DiscordLabel,
        effect = UiEffect.IntentTo(
            Intent(
                Intent.ACTION_VIEW,
                DiscordLink.toUri()
            )
        )
    ),
    MoreItem(
        icon = LibertyFlowIcons.AniLiberty,
        labelRes = AniLibertyLabel,
        effect = UiEffect.IntentTo(
            Intent(
                Intent.ACTION_VIEW,
                AniLibertyLink.toUri()
            )
        )
    )
)

private fun LazyListScope.linkItems(onEffect: (UiEffect) -> Unit) {
    items(
        items = linkItems,
        key = { item -> item.labelRes }
    ) { item ->
        MoreItem(item, onEffect)
    }
}