package com.brbx.design_system.component.precollection

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.brbx.ui_compose.common.BrbxIcon
import com.brbx.ui_compose.common.BrbxText
import com.brbx.ui_compose.components.complex.precollection.precollection.BrbxPrecollection
import com.brbx.ui_compose.components.complex.precollection.precollection.BrbxPrecollectionAppearance
import com.brbx.ui_compose.components.complex.precollection.precollection.rememberCopy
import com.brbx.ui_compose.components.simple.icon.BrbxIcon
import com.brbx.ui_compose.theme.bDimens

@Composable
fun Precollection(
    icon: BrbxIcon?,
    text: BrbxText,
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
        text = text,
        trailingContent = { icon?.let { BrbxIcon(icon, Modifier.size(bDimens.macro2)) } },
    )