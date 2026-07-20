package com.brbx.design_system.component.card.tile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import com.brbx.design_system.common.DesignConstants
import com.brbx.ui_compose.common.UnsafeAppearanceCopy
import com.brbx.ui_compose.components.complex.shimmer.BrbxShimmerBlockAppearances
import com.brbx.ui_compose.components.complex.tile.tile.BrbxTileAppearances
import com.brbx.ui_compose.components.complex.tile.tile.copy
import com.brbx.ui_compose.containers.complex.container.container_with_badge.BrbxContainerWithBadgeAppearances
import com.brbx.ui_compose.theme.bDimens
import com.brbx.ui_compose.theme.mTypography

@Immutable
internal object TileConstants {

    @OptIn(UnsafeAppearanceCopy::class)
    val tileAppearance = BrbxTileAppearances.elevated.copy(
        containerElevationSpotColor = { remember { DesignConstants.elevationColor } },
        containerElevationAmbientColor = { remember { DesignConstants.elevationColor } },
        containerElevation = { DesignConstants.elevation },
        defaultTitleStyle = { mTypography.bodyLarge },
        defaultDescriptionStyle = { mTypography.bodyMedium },
    )
    val iconContainerAppearance = BrbxContainerWithBadgeAppearances.primary
    val iconContainerShimmerAppearance = BrbxShimmerBlockAppearances.default

    val iconSize @Composable @ReadOnlyComposable get() = bDimens.macro2
    val iconPadding @Composable @ReadOnlyComposable get() = bDimens.micro5
}