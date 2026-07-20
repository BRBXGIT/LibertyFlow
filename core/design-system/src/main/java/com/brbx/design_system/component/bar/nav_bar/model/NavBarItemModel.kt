package com.brbx.design_system.component.bar.nav_bar.model

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import com.brbx.ui_compose.common.BrbxText

@Immutable
data class NavBarItemModel(
    @param:DrawableRes val icon: Int,
    val label: BrbxText,
    val destination: NavBarDestination,
)