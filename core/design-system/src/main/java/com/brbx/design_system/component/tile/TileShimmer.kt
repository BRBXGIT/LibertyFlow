package com.brbx.design_system.component.tile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.brbx.ui_compose.components.complex.shimmer.rememberCopy
import com.brbx.ui_compose.components.complex.tile.shimmer.BrbxTileShimmer
import com.brbx.ui_compose.components.complex.tile.tile.BrbxTileAppearance
import com.brbx.ui_compose.containers.complex.container.container_with_badge.BrbxContainerWithBadgeAppearance
import com.brbx.ui_compose.containers.complex.container.shimmer.BrbxContainerShimmer
import com.brbx.ui_compose.theme.BrbxTheme
import com.brbx.ui_compose.theme.mColors

@Composable
fun TileShimmer(
    modifier: Modifier = Modifier,
    appearance: BrbxTileAppearance = TileConstants.tileAppearance,
    iconContainerAppearance: BrbxContainerWithBadgeAppearance =
        TileConstants.iconContainerAppearance
) =
    BrbxTileShimmer(
        modifier = modifier.padding(all = appearance.containerElevation()),
        appearance = appearance,
    ) {
        BrbxContainerShimmer(
            appearance = iconContainerAppearance,
            shimmerAppearance = TileConstants.iconContainerShimmerAppearance.rememberCopy(
                containerColor = { mColors.surfaceContainerHigh },
            ),
        ) {
            Box(
                modifier = Modifier
                    .padding(all = TileConstants.iconPadding)
                    .size(TileConstants.iconSize)
            )
        }
    }

@Preview
@Composable
private fun TileShimmerPreview() {
    BrbxTheme(lightColorScheme()) {
        TileShimmer()
    }
}