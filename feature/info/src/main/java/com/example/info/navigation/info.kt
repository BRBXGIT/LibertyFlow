package com.example.info.navigation

import android.content.ClipData
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.common.navigation.InfoRoute
import com.example.common.ui_helpers.effects.HandleCommonEffects
import com.example.design_system.utils.standardScreenEnterTransition
import com.example.design_system.utils.standardScreenExitTransition
import com.example.info.screen.Info
import com.example.info.screen.InfoEffect
import com.example.info.screen.InfoVM
import kotlinx.coroutines.flow.Flow

fun NavGraphBuilder.info(navController: NavController) = composable<InfoRoute>(
    enterTransition = { standardScreenEnterTransition() },
    exitTransition = { standardScreenExitTransition() }
) {
    val infoVM = hiltViewModel<InfoVM>()

    HandleCommonEffects(
        effects = infoVM.commonEffects,
        navController = navController
    )

    HandleEffects(infoVM.effects)

    Info(
        onCommonEffect = infoVM::sendCommonEffect,
        onEffect = infoVM::sendEffect
    )
}

@Composable
private fun HandleEffects(effects: Flow<InfoEffect>) {
    val clipboard = LocalClipboard.current
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        effects.collect { effect ->
            when(effect) {
                is InfoEffect.CopyVersion -> {
                    val clipEntry = ClipEntry(
                        ClipData.newPlainText(
                            context.getString(effect.versionTextRes),
                            context.getString(effect.versionTextRes)
                        )
                    )
                    clipboard.setClipEntry(clipEntry)
                }
            }
        }
    }
}