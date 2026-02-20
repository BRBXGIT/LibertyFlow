@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.example.design_system.components.bars.bottom_nav_bar

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.graphics.vector.AnimatedImageVector
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
import com.example.common.navigation.CollectionsRoute
import com.example.common.navigation.FavoritesRoute
import com.example.common.navigation.HomeRoute
import com.example.common.navigation.MoreRoute
import com.example.common.navigation.NavigationBase
import com.example.design_system.components.bars.bottom_nav_bar.BottomNavBarConstants.CollectionsLabel
import com.example.design_system.components.bars.bottom_nav_bar.BottomNavBarConstants.FavoritesLabel
import com.example.design_system.components.bars.bottom_nav_bar.BottomNavBarConstants.HomeLabel
import com.example.design_system.components.bars.bottom_nav_bar.BottomNavBarConstants.LABEL_MAX_LINES
import com.example.design_system.components.bars.bottom_nav_bar.BottomNavBarConstants.MoreLabel
import com.example.design_system.components.bars.bottom_nav_bar.BottomNavBarConstants.VISIBLE_OFFSET
import com.example.design_system.components.icons.LibertyFlowAnimatedIcon
import com.example.design_system.theme.icons.LibertyFlowIcons
import com.example.design_system.theme.theme.LibertyFlowTheme
import com.example.design_system.theme.theme.mColors
import com.example.design_system.theme.theme.mMotionScheme

/**
 * Internal representation of a navigation destination within the bottom bar.
 *
 * @property labelRes String resource ID for the item's display name.
 * @property iconRes Drawable resource ID for the [AnimatedImageVector].
 * @property destination The route/base navigation object associated with this item.
 */
private data class NavItem(
    @param:StringRes val labelRes: Int,
    @param:DrawableRes val iconRes: Int, // Animated vector (outlined ↔ filled)
    val destination: NavigationBase
)

/**
 * The static list of items to be displayed in the application's bottom navigation.
 */
private val navItems = listOf(
    NavItem(
        labelRes = HomeLabel,
        iconRes = LibertyFlowIcons.Animated.Home,
        destination = HomeRoute
    ),
    NavItem(
        labelRes = FavoritesLabel,
        iconRes = LibertyFlowIcons.Animated.Heart,
        destination = FavoritesRoute
    ),
    NavItem(
        labelRes = CollectionsLabel,
        iconRes = LibertyFlowIcons.Animated.Book,
        destination = CollectionsRoute
    ),
    NavItem(
        labelRes = MoreLabel,
        iconRes = LibertyFlowIcons.Animated.Dots,
        destination = MoreRoute
    )
)

/**
 * A styled [NavigationBar] that supports animated entry/exit via vertical offsets.
 *
 * @param visible Determines if the bar is translated into view or hidden below the screen.
 * @param onNavItemClick Callback triggered when a new destination is selected.
 * @param selectedRoute The current active destination used to highlight the correct item.
 */
@Composable
fun BoxScope.LibertyFlowBottomNavBar(
    visible: Boolean,
    onNavItemClick: (NavigationBase) -> Unit,
    selectedRoute: NavigationBase?
) {
    val targetOffset = if (visible) VISIBLE_OFFSET.dp else calculateNavBarSize()
    val animatedOffset by animateDpAsState(
        targetValue = targetOffset,
        animationSpec = mMotionScheme.fastSpatialSpec(),
        label = "Animated offset"
    )

    NavigationBar(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .offset(y = animatedOffset)
    ) {
        navItems.forEach { navItem ->
            LibertyFlowBottomNavItem(
                navItem = navItem,
                isSelected = navItem.destination == selectedRoute,
                onClick = { onNavItemClick(navItem.destination) }
            )
        }
    }
}

/**
 * Individual navigation cell within the [LibertyFlowBottomNavBar].
 *
 * This component manages its own [AnimatedImageVector] state, triggering the
 * animation whenever the [isSelected] state transitions.
 *
 * @param navItem The configuration data for this item.
 * @param isSelected Whether this item is currently active.
 * @param onClick Function to execute when the user taps this item.
 */
@Composable
private fun RowScope.LibertyFlowBottomNavItem(
    navItem: NavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    var playAnimation by rememberSaveable { mutableStateOf(false) }

    // Trigger animation when selected state changes
    LaunchedEffect(isSelected) { playAnimation = isSelected }

    NavigationBarItem(
        selected = isSelected,
        onClick = { if (!isSelected) onClick() },
        icon = {
            LibertyFlowAnimatedIcon(
                colorFilter = ColorFilter.tint(mColors.onSecondaryContainer),
                isRunning = playAnimation,
                iconRes = navItem.iconRes
            )
        },
        label = {
            Text(
                text = stringResource(navItem.labelRes),
                maxLines = LABEL_MAX_LINES,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold
            )
        }
    )
}

@Preview
@Composable
private fun LibertyFlowBottomNavBarPreview() {
    LibertyFlowTheme {
        Box {
            LibertyFlowBottomNavBar(
                onNavItemClick = {},
                selectedRoute = HomeRoute,
                visible = true
            )
        }
    }
}