@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.more.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.design_system.theme.icons.LibertyFlowIcons
import com.example.design_system.theme.theme.mTypography
import com.example.more.screen.MoreIntent

private const val ICON_SIZE = 22

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
                Icon(
                    painter = painterResource(LibertyFlowIcons.Outlined.Logout),
                    contentDescription = null,
                    modifier = Modifier.size(ICON_SIZE.dp)
                )
            }
        }
    )
}

private const val TITLE_SPACED_BY = 12

private const val LIBERTY_FLOW = "LibertyFlow"

@Composable
private fun Title() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(TITLE_SPACED_BY.dp)
    ) {
        Icon(
            painter = painterResource(LibertyFlowIcons.Multicolored.LibertyFlow),
            contentDescription = null,
            modifier = Modifier.size(ICON_SIZE.dp),
            tint = Color.Unspecified
        )

        Text(
            text = LIBERTY_FLOW,
            style = mTypography.titleLarge
        )
    }
}