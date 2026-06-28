package com.brbx.design_system.component.nav_bar.composable

import androidx.compose.foundation.layout.height
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.brbx.design_system.component.nav_bar.model.NavBarDestination
import com.brbx.design_system.component.nav_bar.model.NavBarItemModel
import com.brbx.design_system.component.nav_bar.state.rememberNavBarHeight

@Composable
fun NavBar(
    modifier: Modifier = Modifier,
    navItems: List<NavBarItemModel>,
    selectedDestination: NavBarDestination?,
    onItemClick: (NavBarDestination) -> Unit,
) {
    NavigationBar(
        modifier = modifier.height(rememberNavBarHeight())
    ) {
        navItems.forEach { item ->
            NavBarItem(
                item = item,
                selectedDestination = selectedDestination,
                onItemClick = onItemClick,
            )
        }
    }
}

