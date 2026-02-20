@file:OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)

package com.example.anime_details.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.anime_details.R
import com.example.anime_details.screen.AnimeDetailsIntent
import com.example.common.ui_helpers.effects.UiEffect
import com.example.common.vm_helpers.models.LoadingState
import com.example.data.models.auth.UserAuthState
import com.example.data.models.common.request.request_parameters.Collection
import com.example.design_system.containers.DownUpAnimatedContent
import com.example.design_system.theme.icons.LibertyFlowIcons
import com.example.design_system.theme.theme.LibertyFlowTheme
import com.example.design_system.theme.theme.mColors
import com.example.design_system.theme.theme.mMotionScheme
import com.example.design_system.theme.theme.mTypography
import kotlinx.coroutines.delay

private sealed interface TitleState {
    data object Loading: TitleState
    data object Error: TitleState
    data class Content(val title: String): TitleState
    data object Empty: TitleState
}

private sealed interface CollectionState {
    data object Loading: CollectionState
    data object Error: CollectionState
    data object Empty: CollectionState
    data object Added: CollectionState
    data object Unauthorized: CollectionState
}

// Static error title text
private val ErrorLabelRes = R.string.error_label

// Fully transparent top bar background
private const val TOP_BAR_ALPHA = 0f

// IconButton * 2
private val CollectionBoxSize = 48.dp

@Composable
private fun rememberCollectionState(
    userAuthState: UserAuthState,
    collectionsLoadingState: LoadingState,
    activeCollection: Collection?
): CollectionState {
    val state = remember(
        key1 = userAuthState,
        key2 = collectionsLoadingState,
        key3 = activeCollection
    ) {
        when {
            userAuthState !is UserAuthState.LoggedOut && collectionsLoadingState.isLoading -> CollectionState.Loading
            collectionsLoadingState.isError -> CollectionState.Error
            activeCollection != null -> CollectionState.Added
            userAuthState is UserAuthState.LoggedOut -> CollectionState.Unauthorized
            else -> CollectionState.Empty
        }
    }

    return state
}

@Composable
private fun rememberTitleState(
    loadingState: LoadingState,
    englishName: String?
): TitleState {
    val state = remember(
        key1 = loadingState,
        key2 = englishName
    ) {
        when {
            loadingState.isLoading -> TitleState.Loading
            loadingState.isError -> TitleState.Error
            englishName != null -> TitleState.Content(englishName)
            else -> TitleState.Empty
        }
    }

    return state
}

@Composable
internal fun TopBar(
    activeCollection: Collection?,
    collectionsLoadingState: LoadingState,
    userAuthState: UserAuthState,
    englishName: String?,
    loadingState: LoadingState,
    scrollBehavior: TopAppBarScrollBehavior,
    onEffect: (UiEffect) -> Unit,
    onIntent: (AnimeDetailsIntent) -> Unit
) {
    val collectionState = rememberCollectionState(userAuthState, collectionsLoadingState, activeCollection)
    val titleState = rememberTitleState(loadingState, englishName)

    TopAppBar(
        title = {
            DownUpAnimatedContent(targetState = titleState) { target ->
                when (target) {
                    is TitleState.Loading -> AnimatedLoadingText()
                    is TitleState.Error -> TopBarText(stringResource(ErrorLabelRes))
                    is TitleState.Content -> TopBarText(target.title)
                    TitleState.Empty -> Unit
                }
            }
        },
        navigationIcon = {
            TopBarIconButton(
                icon = LibertyFlowIcons.Filled.ArrowLeft,
                onClick = { onEffect(UiEffect.NavigateBack) }
            )
        },
        actions = {
            DownUpAnimatedContent(
                modifier = Modifier.size(CollectionBoxSize),
                targetState = collectionState,
            ) { target ->
                val (icon, onClick) = when (target) {
                    CollectionState.Unauthorized -> LibertyFlowIcons.Outlined.User to {
                        onIntent(AnimeDetailsIntent.ToggleIsAuthBSVisible)
                    }
                    CollectionState.Added -> LibertyFlowIcons.Filled.ListCheck to {
                        onIntent(AnimeDetailsIntent.ToggleCollectionsBSVisible)
                    }
                    CollectionState.Empty -> LibertyFlowIcons.Outlined.ListArrowDown to {
                        onIntent(AnimeDetailsIntent.ToggleCollectionsBSVisible)
                    }
                    CollectionState.Loading -> LibertyFlowIcons.Outlined.Clock to { /* Empty */ }
                    CollectionState.Error -> LibertyFlowIcons.Outlined.DangerCircle to { /* Empty */ }
                }

                TopBarIconButton(icon = icon, onClick = onClick)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = mColors.surfaceContainer.copy(alpha = TOP_BAR_ALPHA),
        ),
        scrollBehavior = scrollBehavior
    )
}

// Loading text content
private val LoadingLabelRes = R.string.loading_label
private const val DOT = "."

// Loading animation timing
private const val DELAY = 200L

@Composable
private fun AnimatedLoadingText() {
    // Controls how many dots are visible
    var dotCount by remember { mutableIntStateOf(0) }

    // Cycles dot visibility
    LaunchedEffect(Unit) {
        while (true) {
            delay(DELAY)
            dotCount = (dotCount + 1) % 4
        }
    }

    // Animates alpha for each dot
    val animatedDots = List(3) { index ->
        animateFloatAsState(
            targetValue = if (index < dotCount) 1f else 0f,
            animationSpec = mMotionScheme.fastEffectsSpec(),
            label = "Title dots loading animation"
        )
    }

    Row {
        Text(text = stringResource(LoadingLabelRes), style = mTypography.titleLarge)
        animatedDots.forEach { alpha ->
            Text(
                text = DOT,
                modifier = Modifier.alpha(alpha.value),
            )
        }
    }
}

// Title text configuration
private const val TEXT_MAX_LINES = 1

@Composable
private fun TopBarText(text: String) {
    Text(
        text = text,
        maxLines = TEXT_MAX_LINES,
        overflow = TextOverflow.Ellipsis,
        style = mTypography.titleLarge
    )
}

// Navigation icon size
private const val ICON_SIZE = 22

@Composable
private fun TopBarIconButton(
    icon: Int,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(ICON_SIZE.dp)
        )
    }
}

@Preview
@Composable
private fun AnimatedLoadingTextPreview() {
    LibertyFlowTheme {
        AnimatedLoadingText()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun AnimeScreenTopBarPreview() {
    LibertyFlowTheme {
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

        TopBar(
            activeCollection = null,
            collectionsLoadingState = LoadingState(),
            userAuthState = UserAuthState.LoggedIn,
            englishName = "Name",
            loadingState = LoadingState(),
            scrollBehavior = scrollBehavior,
            onEffect = {},
            onIntent = {},
        )
    }
}