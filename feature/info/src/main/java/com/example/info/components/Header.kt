package com.example.info.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.design_system.components.icons.LibertyFlowIcon
import com.example.design_system.theme.icons.LibertyFlowIcons
import com.example.design_system.theme.theme.LibertyFlowTheme
import com.example.design_system.theme.theme.mDimens

private val MainBoxHeight = 210.dp
private val IconSize = 84.dp

/**
 * A decorative header for the Information screen list.
 * * Contains the main application icon centered within a fixed-height box
 * and a HorizontalDivider at the bottom.
 */
@Composable
internal fun LazyItemScope.Header() {
    Box(
        modifier = Modifier
            .animateItem()
            .fillMaxWidth()
            .height(MainBoxHeight)
            .padding(horizontal = mDimens.paddingMedium)
    ) {
        LibertyFlowIcon(
            icon = LibertyFlowIcons.Multicolored.LibertyFlow,
            tint = Color.Unspecified,
            modifier = Modifier
                .align(Alignment.Center)
                .size(IconSize),
        )

        HorizontalDivider(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun HeaderPreview() {
    LibertyFlowTheme {
        LazyColumn {
            item {
                Header()
            }
        }
    }
}