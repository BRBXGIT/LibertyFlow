package com.example.onboarding.screen

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.design_system.theme.icons.LibertyFlowIcons
import com.example.onboarding.R

sealed class OnboardingPage(
    @param:StringRes val title: Int,
    @param:StringRes val description: Int,
    @param:DrawableRes val image: Int
) {
    data object Welcome: OnboardingPage(
        title = R.string.welcome_label,
        description = R.string.welcome_description,
        image = LibertyFlowIcons.Illustrations.Welcome
    )

    data object Vpn: OnboardingPage(
        title = R.string.vpn_label,
        description = R.string.vpn_description,
        image = LibertyFlowIcons.Illustrations.Vpn
    )

    data object Permissions: OnboardingPage(
        title = R.string.permissions_label,
        description = R.string.permissions_description,
        image = LibertyFlowIcons.Illustrations.Permissions
    )
}