@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.settings.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.common.ui_helpers.effects.UiEffect
import com.example.data.models.theme.ThemeValue
import com.example.design_system.components.bars.basic_top_bar.BasicTopBar
import com.example.settings.R
import com.example.settings.components.ColorSchemesLR
import com.example.settings.components.SegmentedThemeButton

private val LCPadding = 16.dp

private val LCSpacedBy = 16.dp

private val SettingsLabel = R.string.settings_label

@Composable
fun Settings(
    state: SettingsState,
    onIntent: (SettingsIntent) -> Unit,
    onEffect: (UiEffect) -> Unit
) {
    val topBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        topBar = {
            BasicTopBar(
                label = stringResource(SettingsLabel),
                onNavClick = { onEffect(UiEffect.NavigateBack) },
                scrollBehavior = topBarScrollBehavior
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(topBarScrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(LCSpacedBy),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                bottom = innerPadding.calculateBottomPadding() + LCPadding,
                top = innerPadding.calculateTopPadding() + LCPadding
            )
        ) {
            theme(state, onIntent)

            colorScheme(state, onIntent)
        }
    }
}

private const val THEME_KEY = "ThemeKey"

private fun LazyListScope.theme(
    state: SettingsState,
    onIntent: (SettingsIntent) -> Unit
) {
    item(
        key = THEME_KEY
    ) {
        SegmentedThemeButton(
            onIntent = onIntent,
            state = state
        )
    }
}

private const val COLOR_SCHEME_KEY = "ColorSchemeKey"

private fun LazyListScope.colorScheme(
    state: SettingsState,
    onIntent: (SettingsIntent) -> Unit
) {
    if (state.themeSettings.userThemePreference != ThemeValue.DYNAMIC) {
        item(
            key = COLOR_SCHEME_KEY
        ) {
            ColorSchemesLR(
                state = state,
                onIntent = onIntent
            )
        }
    }
}