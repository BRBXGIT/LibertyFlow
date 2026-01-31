@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.design_system.components.bars.basic_top_bar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.design_system.theme.LibertyFlowIcons
import com.example.design_system.theme.mTypography

@Composable
fun BasicTopBar(
    label: String,
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
                Icon(
                    painter = painterResource(LibertyFlowIcons.ArrowLeftFilled),
                    contentDescription = null
                )
            }
        }
    )
}