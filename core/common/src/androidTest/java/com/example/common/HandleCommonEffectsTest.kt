package com.example.common

import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.NavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.common.navigation.AnimeDetailsRoute
import com.example.common.ui_helpers.effects.HandleCommonEffects
import com.example.common.ui_helpers.effects.UiEffect
import io.mockk.Awaits
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HandleCommonEffectsTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val navController = mockk<NavController>()
    private val snackbarHostState = mockk<SnackbarHostState>()
    private val effectsFlow = Channel<UiEffect>(Channel.BUFFERED)

    // --- Navigation ---
    @Test
    fun handleNavigationEffect_callsNavController() = runTest {
        val testRoute = AnimeDetailsRoute(1)
        every { navController.navigate(testRoute) } just Runs

        composeTestRule.setContent {
            HandleCommonEffects(
                effects = effectsFlow.receiveAsFlow(),
                navController = navController
            )
        }

        effectsFlow.send(UiEffect.Navigate(testRoute))

        composeTestRule.awaitIdle()

        verify { navController.navigate(testRoute) }
    }

    @Test
    fun handleNavigationBackEffect_callsNavController() = runTest {
        every { navController.navigateUp() } returns true

        composeTestRule.setContent {
            HandleCommonEffects(
                effects = effectsFlow.receiveAsFlow(),
                navController = navController
            )
        }

        effectsFlow.send(UiEffect.NavigateBack)

        composeTestRule.awaitIdle()

        verify { navController.navigateUp() }
    }

    // --- Intents ---
    @Test
    fun handleIntentToEffect_callsStartActivity() = runTest {
        val mockContext = mockk<Context>()
        val intent = Intent()
        every { mockContext.startActivity(intent) } just Runs

        composeTestRule.setContent {
            HandleCommonEffects(
                effects = effectsFlow.receiveAsFlow(),
                navController = navController,
                context = mockContext
            )
        }

        effectsFlow.send(UiEffect.IntentTo(intent))

        composeTestRule.awaitIdle()

        verify { mockContext.startActivity(intent) }
    }

    // --- Snackbars ---
    @Test
    fun handleShowSnackbarWithActionEffect_callsSnackbarHost() = runTest {
        coEvery { snackbarHostState.showSnackbar(message = any()) } returns SnackbarResult.Dismissed

        composeTestRule.setContent {
            HandleCommonEffects(
                snackbarHostState = snackbarHostState,
                effects = effectsFlow.receiveAsFlow(),
                navController = navController
            )
        }

        effectsFlow.send(UiEffect.ShowSnackbarWithAction(messageRes = com.example.data.R.string.internet_message))

        composeTestRule.awaitIdle()

        coVerify { snackbarHostState.showSnackbar(message = any()) }
    }

    @Test
    fun handleShowSimpleSnackbar_callsSnackbarHost() = runTest {
        coEvery {
            snackbarHostState.showSnackbar(
                message = any(),
                actionLabel = any(),
                withDismissAction = any(),
                duration = any()
            )
        } returns SnackbarResult.Dismissed

        composeTestRule.setContent {
            HandleCommonEffects(
                snackbarHostState = snackbarHostState,
                effects = effectsFlow.receiveAsFlow(),
                navController = navController
            )
        }

        effectsFlow.send(UiEffect.ShowSimpleSnackbar(messageRes = com.example.data.R.string.internet_message))

        composeTestRule.awaitIdle()

        coVerify {
            snackbarHostState.showSnackbar(
                message = any(),
                withDismissAction = true,
                actionLabel = any(),
                duration = any()
            )
        }
    }
}