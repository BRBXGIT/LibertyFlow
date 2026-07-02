package com.brbx.design_system.component.rainbow_button

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.brbx.ui_compose.components.complex.shimmer.BrbxShimmerBlockAppearance
import com.brbx.ui_compose.components.complex.shimmer.BrbxShimmerBlockAppearances
import com.brbx.ui_compose.containers.complex.animated_border.animated_border.BrbxAnimatedBorderContainerAppearance
import com.brbx.ui_compose.containers.complex.animated_border.shimmer.BrbxAnimatedBorderContainerShimmer
import com.brbx.ui_compose.state.brbxRememberTextHeightInDp
import com.brbx.ui_compose.theme.BrbxTheme
import com.brbx.ui_compose.theme.bDimens

@Composable
fun RainbowButtonShimmer(
    modifier: Modifier = Modifier,
    shimmerAppearance: BrbxShimmerBlockAppearance = BrbxShimmerBlockAppearances.default,
    appearance: BrbxAnimatedBorderContainerAppearance =
        RainbowButtonConstants.ButtonAppearance,
) =
    BrbxAnimatedBorderContainerShimmer(
        modifier = modifier.padding(all = appearance.shadowElevation()),
        appearance = appearance,
        shimmerAppearance = shimmerAppearance,
    ) {
        val textHeight = appearance.textStyle().brbxRememberTextHeightInDp()
        val height = if (textHeight <= RainbowButtonConstants.IconSize) {
            RainbowButtonConstants.IconSize
        } else textHeight
        Box(
            modifier = Modifier
                .padding(all = RainbowButtonConstants.ButtonContentPadding)
                .height(height)
        )
    }

@Preview(showSystemUi = true)
@Composable
private fun RainbowButtonShimmerPreview() {
    BrbxTheme(lightColorScheme()) {
        RainbowButtonShimmer(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = bDimens.micro8)
        )
    }
}