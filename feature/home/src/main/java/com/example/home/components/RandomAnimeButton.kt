package com.example.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.design_system.theme.LibertyFlowIcons
import com.example.design_system.theme.mTypography
import com.example.home.R

internal object RandomAnimeButtonConstants {
    const val CONTENT_SPACED_BY = 16

    val ButtonLabel = R.string.random_button_label
    const val RANDOM_BUTTON_KEY = "RANDOM_BUTTON_KEY"
}

@Composable
fun LazyGridItemScope.RandomAnimeButton(
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .animateItem()
            .fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(RandomAnimeButtonConstants.CONTENT_SPACED_BY.dp)
        ) {
            Icon(
                painter = painterResource(LibertyFlowIcons.FunnyCube),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )

            Text(
                text = stringResource(RandomAnimeButtonConstants.ButtonLabel),
                style = mTypography.bodyMedium
            )
        }
    }
}