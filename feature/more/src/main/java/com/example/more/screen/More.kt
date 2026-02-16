@file:OptIn(ExperimentalMaterial3Api::class)

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.net.toUri
import com.example.common.navigation.InfoRoute
import com.example.common.navigation.SettingsRoute
import com.example.common.ui_helpers.effects.UiEffect
import com.example.design_system.components.bars.bottom_nav_bar.calculateNavBarSize
import com.example.design_system.components.dividers.dividerWithLabel
import com.example.design_system.containers.M3Container
import com.example.design_system.theme.icons.LibertyFlowIcons
import com.example.design_system.theme.theme.mColors
import com.example.design_system.theme.theme.mDimens
import com.example.more.R
import com.example.more.components.LogoutDialog
import com.example.more.components.MoreItem
import com.example.more.components.TopBar

private val LINKS_LABEL = R.string.links_label
private val APP_LABEL = R.string.app_label

/**
 * The primary screen for the 'More' section, containing app settings and external links.
 * * Orchestrates the [Scaffold] layout, integrating a [TopBar], [SnackbarHost], and a
 * [LazyColumn] of settings groups. It also manages the visibility of the [LogoutDialog].
 *
 * @param snackbarHostState State for displaying system messages (e.g., logout success).
 * @param state The current UI state containing dialog visibility flags.
 * @param onEffect Callback for processing navigation or external intent side effects.
 * @param onIntent Callback for processing user actions like toggling the logout dialog.
 */
@Composable
internal fun More(
    snackbarHostState: SnackbarHostState,
    state: MoreState,
    onEffect: (UiEffect) -> Unit,
    onIntent: (MoreIntent) -> Unit
) {
    val topBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { TopBar(topBarScrollBehavior, onIntent) },
        contentWindowInsets = WindowInsets(bottom = calculateNavBarSize()),
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        if (state.isLogoutADVisible) LogoutDialog(onIntent)

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
                verticalArrangement = Arrangement.spacedBy(mDimens.paddingMedium),
                contentPadding = PaddingValues(vertical = mDimens.paddingMedium)
            ) {
                dividerWithLabel(APP_LABEL)

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
        iconRes = LibertyFlowIcons.Outlined.Settings,
        labelRes = SettingsLabel,
        originalColor = false,
        effect = UiEffect.Navigate(SettingsRoute)
    ),
    MoreItem(
        iconRes = LibertyFlowIcons.Outlined.Info,
        labelRes = InfoLabel,
        originalColor = false,
        effect = UiEffect.Navigate(InfoRoute)
    )
)

private const val APP_ITEMS_KEY = "AppItemsKey"

/**
 * Extension on [LazyListScope] to render the application-specific settings group.
 * Items are wrapped in an [M3Container] to visually group related settings (Settings, Info).
 */
private fun LazyListScope.appItems(onEffect: (UiEffect) -> Unit) {
    item(
        key = APP_ITEMS_KEY
    ) {
        M3Container(Modifier.animateItem()) {
            appItems.forEach { item ->
                MoreItem(item, onEffect)
            }
        }
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
        iconRes = LibertyFlowIcons.Multicolored.VK,
        labelRes = VKLabel,
        effect = UiEffect.IntentTo(
            Intent(
                Intent.ACTION_VIEW,
                VKLink.toUri()
            )
        )
    ),
    MoreItem(
        iconRes = LibertyFlowIcons.Multicolored.YouTube,
        labelRes = YouTubeLabel,
        effect = UiEffect.IntentTo(
            Intent(
                Intent.ACTION_VIEW,
                YouTubeLink.toUri()
            )
        )
    ),
    MoreItem(
        iconRes = LibertyFlowIcons.Multicolored.Patreon,
        labelRes = PatreonLabel,
        effect = UiEffect.IntentTo(
            Intent(
                Intent.ACTION_VIEW,
                PatreonLink.toUri()
            )
        )
    ),
    MoreItem(
        iconRes = LibertyFlowIcons.Multicolored.Telegram,
        labelRes = TelegramLabel,
        effect = UiEffect.IntentTo(
            Intent(
                Intent.ACTION_VIEW,
                TelegramLink.toUri()
            )
        )
    ),
    MoreItem(
        iconRes = LibertyFlowIcons.Multicolored.Discord,
        labelRes = DiscordLabel,
        effect = UiEffect.IntentTo(
            Intent(
                Intent.ACTION_VIEW,
                DiscordLink.toUri()
            )
        )
    ),
    MoreItem(
        iconRes = LibertyFlowIcons.Multicolored.AniLiberty,
        labelRes = AniLibertyLabel,
        effect = UiEffect.IntentTo(
            Intent(
                Intent.ACTION_VIEW,
                AniLibertyLink.toUri()
            )
        )
    )
)

private const val LINK_ITEMS_KEY = "LinksItemsKey"

/**
 * Extension on [LazyListScope] to render external social and community links.
 * Items use multi-colored brand icons and trigger external [Intent]s.
 */
private fun LazyListScope.linkItems(onEffect: (UiEffect) -> Unit) {
    item(
        key = LINK_ITEMS_KEY
    ) {
        M3Container(Modifier.animateItem()) {
            linkItems.forEach { item ->
                MoreItem(item, onEffect)
            }
        }
    }
}