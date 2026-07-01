package com.brbx.design_system.component.tile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.brbx.design_system.component.precollection.Precollection
import com.brbx.design_system.component.precollection.PrecollectionModel
import com.brbx.ui_compose.common.toBrbxIcon
import com.brbx.ui_compose.common.toBrbxText
import com.brbx.ui_compose.components.complex.tile.tile.BrbxTile
import com.brbx.ui_compose.components.complex.tile.tile.BrbxTileAppearance
import com.brbx.ui_compose.components.simple.icon.BrbxIcon
import com.brbx.ui_compose.containers.complex.container.container_with_badge.BrbxContainerWithBadge
import com.brbx.ui_compose.containers.complex.container.container_with_badge.BrbxContainerWithBadgeAppearance
import com.brbx.ui_compose.theme.BrbxTheme
import com.brbx.ui_compose.theme.bMotion
import dev.chiksmedina.solar.BoldSolar
import dev.chiksmedina.solar.bold.Users
import dev.chiksmedina.solar.bold.users.User

// TODO Add new episodes tile
@Composable
fun Tile(
    model: TileModel,
    modifier: Modifier = Modifier,
    appearance: BrbxTileAppearance = TileConstants.tileAppearance,
    iconContainerAppearance: BrbxContainerWithBadgeAppearance =
        TileConstants.iconContainerAppearance,
) {
    BrbxTile(
        appearance = appearance,
        modifier = modifier
            .animateContentSize(animationSpec = bMotion.mediumSpatialSpec())
            .padding(all = appearance.containerElevation()),
        title = model.title,
        description = model.description,
        trailingContent = {
            BrbxContainerWithBadge(
                appearance = iconContainerAppearance,
            ) {
                BrbxIcon(
                    brbxIcon = model.icon,
                    modifier = Modifier
                        .padding(all = TileConstants.iconPadding)
                        .size(TileConstants.iconSize)
                )
            }
        },
        additionalContent = {
            model.precollection?.let { p ->
                AnimatedVisibility(
                    visible = model.isPrecollectionVisible,
                    enter = fadeIn(animationSpec = bMotion.nonSpatialMediumSpec()) +
                            slideInVertically(animationSpec = bMotion.mediumSpatialSpec()),
                    exit = fadeOut(animationSpec = bMotion.nonSpatialMediumSpec()) +
                            slideOutVertically(animationSpec = bMotion.mediumSpatialSpec()),
                ) {
                    Precollection(
                        appearance = p.appearance,
                        model = p,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        },
    )
}

@Preview(showSystemUi = true)
@Composable
private fun TilePreview() {
    BrbxTheme(lightColorScheme()) {
        Tile(
            model = TileModel(
                title = "Title".toBrbxText(),
                description = "Description bla bla".toBrbxText(),
                icon = BoldSolar.Users.User.toBrbxIcon(),
                onClick = {},
                precollection = PrecollectionModel(
                    text = "Precollection".toBrbxText(),
                    icon = BoldSolar.Users.User.toBrbxIcon(),
                    onClick = {},
                )
            ),
        )
    }
}