package com.brbx.design_system.component.tile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import com.brbx.design_system.common.DesignConstants
import com.brbx.design_system.common.rememberElevationColor
import com.brbx.ui_compose.components.complex.shimmer.BrbxShimmerBlockAppearances
import com.brbx.ui_compose.components.complex.tile.tile.BrbxTileAppearances
import com.brbx.ui_compose.components.complex.tile.tile.rememberCopy
import com.brbx.ui_compose.containers.complex.container.container_with_badge.BrbxContainerWithBadgeAppearances
import com.brbx.ui_compose.theme.bDimens
import com.brbx.ui_compose.theme.mTypography

@Immutable
internal object TileConstants {
    val tileAppearance @Composable get() = BrbxTileAppearances.elevated.rememberCopy(
        containerElevationSpotColor = { rememberElevationColor() },
        containerElevationAmbientColor = { rememberElevationColor() },
        containerElevation = { DesignConstants.elevation },
        defaultTitleStyle = { mTypography.bodyLarge },
        defaultDescriptionStyle = { mTypography.bodyMedium },
    )
    val iconContainerAppearance = BrbxContainerWithBadgeAppearances.primary
    val iconContainerShimmerAppearance = BrbxShimmerBlockAppearances.default

    val iconSize @Composable @ReadOnlyComposable get() = bDimens.macro2
    val iconPadding @Composable @ReadOnlyComposable get() = bDimens.micro5
}