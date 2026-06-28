package com.brbx.design_system.component.anime_card

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.brbx.design_system.common.DesignConstants
import com.brbx.design_system.common.rememberElevationColor
import com.brbx.ui_compose.components.complex.content_card.content_card.BrbxContentCardAppearances
import com.brbx.ui_compose.components.complex.content_card.content_card.rememberCopy
import com.brbx.ui_compose.theme.mTypography

@Immutable
internal object AnimeCardConstants {

    val animeCardAppearance @Composable get() =
        BrbxContentCardAppearances.tertiaryElevated.rememberCopy(
            defaultTitleStyle = { mTypography.bodyMedium },
            defaultDescriptionStyle = { mTypography.bodySmall },
            containerElevationSpotColor = { rememberElevationColor() },
            containerElevationAmbientColor = { rememberElevationColor() },
            containerElevation = { DesignConstants.elevation }
        )
}