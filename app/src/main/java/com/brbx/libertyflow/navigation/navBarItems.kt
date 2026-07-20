package com.brbx.libertyflow.navigation

import com.brbx.design_system.component.bar.nav_bar.model.NavBarItemModel
import com.brbx.home.navigation.HomeRoute
import com.brbx.libertyflow.model.AppDrawable
import com.brbx.libertyflow.model.AppString
import com.brbx.ui_compose.common.toBrbxText

internal val navBarItems = listOf(
    NavBarItemModel(
        icon = AppDrawable.ic_home_animated,
        label = AppString.home.toBrbxText(),
        destination = HomeRoute,
    )
)