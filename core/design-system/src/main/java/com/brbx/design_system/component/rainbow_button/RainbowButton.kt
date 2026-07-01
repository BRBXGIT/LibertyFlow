package com.brbx.design_system.component.rainbow_button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brbx.ui_compose.common.BrbxIcon
import com.brbx.ui_compose.common.BrbxText
import com.brbx.ui_compose.common.asString
import com.brbx.ui_compose.common.toBrbxIcon
import com.brbx.ui_compose.common.toBrbxText
import com.brbx.ui_compose.components.simple.icon.BrbxIcon
import com.brbx.ui_compose.containers.complex.animated_border.animated_border.BrbxAnimatedBorderContainer
import com.brbx.ui_compose.containers.complex.animated_border.animated_border.BrbxAnimatedBorderContainerAppearance
import com.brbx.ui_compose.theme.BrbxTheme
import com.brbx.ui_compose.theme.bDimens
import com.brbx.ui_compose.theme.mTypography
import dev.chiksmedina.solar.OutlineSolar
import dev.chiksmedina.solar.outline.EssentionalUi
import dev.chiksmedina.solar.outline.essentionalui.Cat
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun RainbowButton(
    text: BrbxText,
    showAnimation: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: BrbxIcon? = null,
    appearance: BrbxAnimatedBorderContainerAppearance =
        RainbowButtonConstants.ButtonAppearance,
) {
    BrbxAnimatedBorderContainer(
        appearance = appearance,
        showAnimation = showAnimation,
        onClick = onClick,
        modifier = modifier.padding(all = appearance.shadowElevation()),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(bDimens.micro8),
            modifier = Modifier
                .align(Alignment.Center)
                .padding(all = RainbowButtonConstants.ButtonContentPadding),
        ) {
            icon?.let {
                BrbxIcon(
                    brbxIcon = icon,
                    modifier = Modifier.size(RainbowButtonConstants.IconSize),
                )
            }

            Text(
                text = text.asString(),
                style = mTypography.labelLarge,
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun RainbowButtonPreview() {
    var show by remember { mutableStateOf(false) }
    LaunchedEffect(show) {
        if (show) {
            delay(5000.milliseconds)
            show = false
        }
    }

    BrbxTheme(lightColorScheme()) {
        Column(
            verticalArrangement = Arrangement.spacedBy(100.dp)
        ) {
            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth().padding(horizontal = bDimens.micro8)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(bDimens.micro8),
                ) {
                    BrbxIcon(OutlineSolar.EssentionalUi.Cat)

                    Text(
                        text = "This is cat",
                        style = mTypography.labelLarge,
                    )
                }
            }

            RainbowButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = bDimens.micro8),
                text = "This is cat".toBrbxText(),
                showAnimation = show,
                onClick = { show = true },
                icon = OutlineSolar.EssentionalUi.Cat.toBrbxIcon(),
            )
        }
    }
}