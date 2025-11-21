package com.example.home.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.design_system.components.buttons.ButtonWithIcon
import com.example.design_system.theme.LibertyFlowIcons
import com.example.home.R
import com.example.home.screen.HomeIntent

internal object RandomAnimeButtonConstants {

    val ButtonLabel = R.string.random_button_label
    const val RANDOM_BUTTON_KEY = "RANDOM_BUTTON_KEY"
}

@Composable
fun LazyGridItemScope.RandomAnimeButton(
    onIntent: (HomeIntent) -> Unit
) {
    ButtonWithIcon(
        text = stringResource(RandomAnimeButtonConstants.ButtonLabel),
        icon = LibertyFlowIcons.FunnyCube,
        onClick = { onIntent(HomeIntent.GetRandomAnime) },
        modifier = Modifier
            .animateItem()
            .fillMaxWidth()
    )
}