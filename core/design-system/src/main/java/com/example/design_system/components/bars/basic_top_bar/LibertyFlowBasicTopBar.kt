@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.design_system.components.bars.basic_top_bar

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.design_system.components.icons.LibertyFlowIcon
import com.example.design_system.theme.icons.LibertyFlowIcons
import com.example.design_system.theme.theme.LibertyFlowTheme
import com.example.design_system.theme.theme.mTypography

private val IconSize = 22.dp

/**
 * A simplified top app bar component tailored for consistent navigation and branding.
 *
 * This composable wraps the standard [TopAppBar], providing a pre-configured navigation
 * icon and consistent typography styling.
 *
 * @param label The text to be displayed as the title in the center/start of the bar.
 * @param onNavClick Callback invoked when the user taps the navigation (back) icon.
 * @param scrollBehavior The [TopAppBarScrollBehavior] that determines how the bar
 * responds to scroll events in a linked scrollable container.
 */
@Composable
fun LibertyFlowBasicTopBar(
    @StringRes label: String,
    onNavClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    TopAppBar(
        scrollBehavior = scrollBehavior,
        title = { Text(label, style = mTypography.titleLarge) },
        navigationIcon = {
            IconButton(
                onClick = onNavClick
            ) {
                LibertyFlowIcon(
                    icon = LibertyFlowIcons.Filled.ArrowLeft,
                    modifier = Modifier.size(IconSize)
                )
            }
        }
    )
}

@Preview
@Composable
private fun LibertyFlowBasicTopBarPreview() {
    LibertyFlowTheme {
        LibertyFlowBasicTopBar(
            label = "Basic top bar",
            onNavClick = {},
            scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
        )
    }
}