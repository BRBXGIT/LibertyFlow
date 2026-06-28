package com.brbx.design_system.container

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.brbx.ui_compose.components.complex.pull_to_refresh_indicator.BrbxPullToRefreshDefaultIndicator
import com.brbx.ui_compose.containers.complex.pull_to_refresh.BrbxPullToRefreshContainer
import com.brbx.ui_compose.containers.complex.pull_to_refresh.BrbxPullToRefreshContainerAppearances
import com.brbx.ui_compose.containers.complex.pull_to_refresh.rememberCopy
import com.brbx.ui_compose.theme.bDimens
import com.brbx.ui_compose.theme.bMotion

@Composable
fun PullToRefreshContainer(
    onRefresh: () -> Unit,
    isRefreshing: Boolean,
    minimalisticIndicator: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) =
    BrbxPullToRefreshContainer(
        modifier = modifier,
        appearance = BrbxPullToRefreshContainerAppearances.withVibration().rememberCopy(
            translationAnimationSpec = { bMotion.bouncyEffectSpec() },
        ),
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        content = content,
        indicator = { fraction ->
            if (minimalisticIndicator) {
                if (isRefreshing) {
                    MinimalisticIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = bDimens.micro8)
                    )
                }
            } else {
                BrbxPullToRefreshDefaultIndicator(isRefreshing, fraction)
            }
        }
    )

@Composable
private fun MinimalisticIndicator(
    modifier: Modifier = Modifier
) {
    LinearWavyProgressIndicator(modifier)
}