package com.brbx.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.brbx.home.view_model.view_model.ViewModel
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.home() = composable<HomeRoute> {
    val viewModel = koinViewModel<ViewModel>()
}