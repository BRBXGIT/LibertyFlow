package com.brbx.design_system.container

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.brbx.design_system.component.error_screen.ErrorScreen
import com.brbx.ui_compose.containers.complex.scaffold.BrbxShimmerScaffold
import com.brbx.ui_compose.containers.complex.scaffold.BrbxShimmerScaffoldAppearance
import com.brbx.ui_compose.containers.complex.scaffold.BrbxShimmerScaffoldAppearances
import com.brbx.ui_compose.theme.bDimens

@Composable
fun ShimmerScaffold(
    isShimmering: Boolean,
    modifier: Modifier = Modifier,
    appearance: BrbxShimmerScaffoldAppearance = BrbxShimmerScaffoldAppearances.default,
    isError: Boolean,
    onShimmerEnd: (Boolean) -> Unit = {},
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    errorContent: @Composable (PaddingValues) -> Unit = { paddingValues ->
        ErrorScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(all = bDimens.micro8)
        )
    },
    shimmerContent: @Composable (PaddingValues) -> Unit,
    content: @Composable (PaddingValues) -> Unit,
) =
    BrbxShimmerScaffold(
        onShimmeringFinished = onShimmerEnd,
        isError = isError,
        appearance = appearance,
        modifier = modifier,
        isShimmering = isShimmering,
        snackbarHost = snackbarHost,
        topBar = topBar,
        shimmerContent = shimmerContent,
        content = content,
        bottomBar = bottomBar,
        floatingActionButton = floatingActionButton,
        errorContent = errorContent,
    )