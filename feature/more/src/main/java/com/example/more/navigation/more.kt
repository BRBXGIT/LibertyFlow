package com.example.more.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.common.navigation.MoreRoute
import com.example.common.ui_helpers.HandleCommonEffects
import com.example.more.screen.More
import com.example.more.screen.MoreVM

fun NavGraphBuilder.more(
    navController: NavController
) = composable<MoreRoute> {
    val moreVM = hiltViewModel<MoreVM>()

    val moreEffects = moreVM.moreEffects

    HandleCommonEffects(moreEffects, navController)

    More(moreVM::sendEffect)
}