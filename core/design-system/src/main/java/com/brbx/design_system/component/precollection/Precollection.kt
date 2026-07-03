package com.brbx.design_system.component.precollection

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.brbx.ui_compose.common.toBrbxIcon
import com.brbx.ui_compose.common.toBrbxText
import com.brbx.ui_compose.components.complex.precollection.precollection.BrbxPrecollection
import com.brbx.ui_compose.components.complex.precollection.precollection.BrbxPrecollectionAppearance
import com.brbx.ui_compose.components.complex.precollection.precollection.rememberCopy
import com.brbx.ui_compose.components.simple.icon.BrbxIcon
import com.brbx.ui_compose.theme.BrbxTheme
import com.brbx.ui_compose.theme.bDimens
import dev.chiksmedina.solar.BoldSolar
import dev.chiksmedina.solar.bold.Arrows
import dev.chiksmedina.solar.bold.arrows.RoundArrowRight

@Composable
fun Precollection(
    model: PrecollectionModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    appearance: BrbxPrecollectionAppearance = PrecollectionConstants.precollectionAppearance,
) =
    BrbxPrecollection(
        appearance = appearance.rememberCopy(
            contentPadding = { PaddingValues(all = bDimens.micro5) },
        ),
        onClick = onClick,
        modifier = modifier,
        text = model.text,
        trailingContent = { BrbxIcon(model.icon, Modifier.size(bDimens.macro2)) },
    )

@Preview
@Composable
private fun PrecollectionPreview() {
    BrbxTheme(lightColorScheme()) {
        Precollection(
            onClick = {},
            model = PrecollectionModel(
                text = "Bla bla title text".toBrbxText(),
                icon = BoldSolar.Arrows.RoundArrowRight.toBrbxIcon(),
            ),
        )
    }
}