package com.brbx.design_system.component.card.anime_card

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import com.brbx.design_system.common.DesignConstants
import com.brbx.ui_compose.common.UnsafeAppearanceCopy
import com.brbx.ui_compose.components.complex.content_card.content_card.BrbxContentCardAppearances
import com.brbx.ui_compose.components.complex.content_card.content_card.copy
import com.brbx.ui_compose.theme.mColors
import com.brbx.ui_compose.theme.mTypography

@Immutable
internal object AnimeCardConstants {

    val Width = 150.dp
    val Height = 270.dp

    // TODO Maybe make info surface color
    @OptIn(UnsafeAppearanceCopy::class)
    val AnimeCardAppearance =
        BrbxContentCardAppearances.secondaryElevated.copy(
            defaultTitleStyle = { mTypography.bodyMedium },
            defaultDescriptionStyle = { mTypography.bodySmall },
            containerElevationSpotColor = { remember { DesignConstants.elevationColor } },
            containerElevationAmbientColor = { remember { DesignConstants.elevationColor } },
            containerElevation = { DesignConstants.elevation },
        )
    val selectedBorderColor @Composable @ReadOnlyComposable get() = mColors.tertiary
}