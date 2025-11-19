@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.example.design_system.components.bars.bottom_nav_bar

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.common.navigation.FavoritesRoute
import com.example.common.navigation.HomeRoute
import com.example.common.navigation.NavBarBase
import com.example.common.navigation.NavigationBase
import com.example.design_system.theme.LibertyFlowIcons
import com.example.design_system.theme.LibertyFlowTheme
import com.example.design_system.theme.mColors
import com.example.design_system.theme.mMotionScheme

private data class NavItem(
    val labelRes: Int,
    val iconRes: Int, // Animated vector (outlined ↔ filled)
    val destination: NavigationBase
)

private val navItems = listOf(
    NavItem(
        labelRes = BottomNavBarConstants.HomeLabel,
        iconRes = LibertyFlowIcons.HomeAnimated,
        destination = HomeRoute
    ),
    NavItem(
        labelRes = BottomNavBarConstants.FavoritesLabel,
        iconRes = LibertyFlowIcons.HeartAnimated,
        destination = FavoritesRoute
    )
)

@Composable
fun BoxScope.BottomNavBar(
    onNavItemClick: (NavigationBase) -> Unit,
    selectedRoute: NavigationBase
) {
    val visible = selectedRoute is NavBarBase
    val targetOffset = if (visible) 0.dp else BottomNavBarConstants.BOTTOM_BAR_HEIGHT.dp
    val animatedOffset by animateDpAsState(
        targetValue = targetOffset,
        animationSpec = mMotionScheme.fastSpatialSpec(),
        label = "bottom_nav_bar_offset"
    )

    NavigationBar(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .offset(y = animatedOffset)
    ) {
        navItems.forEach { navItem ->
            BottomNavItem(
                navItem = navItem,
                isSelected = navItem.destination == selectedRoute,
                onClick = { onNavItemClick(navItem.destination) }
            )
        }
    }
}

@Composable
private fun RowScope.BottomNavItem(
    navItem: NavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    var playAnimation by rememberSaveable { mutableStateOf(false) }

    // Trigger animation when selected state changes
    LaunchedEffect(isSelected) { playAnimation = isSelected }

    val animatedVector = AnimatedImageVector.animatedVectorResource(navItem.iconRes)
    val painter = rememberAnimatedVectorPainter(
        animatedImageVector = animatedVector,
        atEnd = playAnimation
    )

    NavigationBarItem(
        selected = isSelected,
        onClick = { if (!isSelected) onClick() },
        icon = {
            Image(
                painter = painter,
                contentDescription = null,
                colorFilter = ColorFilter.tint(mColors.onSecondaryContainer)
            )
        },
        label = {
            Text(
                text = stringResource(navItem.labelRes),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold
            )
        }
    )
}

@Preview
@Composable
private fun BottomNavBarPreview() {
    LibertyFlowTheme {
        Box {
            BottomNavBar(
                onNavItemClick = {},
                selectedRoute = HomeRoute
            )
        }
    }
}