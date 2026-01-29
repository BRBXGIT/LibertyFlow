@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.info.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.common.ui_helpers.effects.UiEffect
import com.example.design_system.components.bars.basic_top_bar.BasicTopBar
import com.example.design_system.theme.mColors
import com.example.info.R
import com.example.info.components.Header

private val InfoLabel = R.string.info_label

@Composable
internal fun Info(onEffect: (UiEffect) -> Unit) {
    val topBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        topBar = {
            BasicTopBar(
                label = stringResource(InfoLabel),
                onNavClick = { onEffect(UiEffect.NavigateBack) },
                scrollBehavior = topBarScrollBehavior
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(topBarScrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(mColors.background)
                .padding(innerPadding)
        ) {
            Header()
        }
    }
}