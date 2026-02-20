@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.more.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.design_system.components.icons.LibertyFlowIcon
import com.example.design_system.theme.icons.LibertyFlowIcons
import com.example.design_system.theme.theme.LibertyFlowTheme
import com.example.design_system.theme.theme.mTypography
import com.example.more.screen.MoreIntent

private const val ICON_SIZE = 22

/**
 * The top navigation bar for the 'More' screen.
 * * Features a branded [Title] and a logout action button. It integrates with
 * [TopAppBarScrollBehavior] to support dynamic elevation or collapsing effects
 * during list scrolling.
 *
 * @param scrollBehavior Controls the visual state of the bar in response to scroll events.
 * @param onIntent Callback to handle UI actions, specifically [MoreIntent.ToggleLogoutDialog].
 */
@Composable
internal fun TopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onIntent: (MoreIntent) -> Unit
) {
    TopAppBar(
        scrollBehavior = scrollBehavior,
        title = { Title() },
        actions = {
            IconButton(
                onClick = { onIntent(MoreIntent.ToggleLogoutDialog) }
            ) {
                LibertyFlowIcon(
                    icon = LibertyFlowIcons.Outlined.Logout,
                    modifier = Modifier.size(ICON_SIZE.dp)
                )
            }
        }
    )
}

private const val TITLE_SPACED_BY = 12

private const val LIBERTY_FLOW = "LibertyFlow"

/**
 * Renders the branded title for the top bar.
 * * Combines the application icon (using its native multi-color palette)
 * with the application name in a horizontal layout.
 */
@Composable
private fun Title() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(TITLE_SPACED_BY.dp)
    ) {
        LibertyFlowIcon(
            icon = LibertyFlowIcons.Multicolored.LibertyFlow,
            tint = Color.Unspecified,
            modifier = Modifier.size(ICON_SIZE.dp),
        )

        Text(
            text = LIBERTY_FLOW,
            style = mTypography.titleLarge
        )
    }
}

@Preview
@Composable
private fun TopBarPreview() {
    LibertyFlowTheme {
        TopBar(
           scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
        ) {}
    }
}