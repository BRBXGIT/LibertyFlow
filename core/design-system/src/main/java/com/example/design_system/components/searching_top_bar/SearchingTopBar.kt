@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.design_system.components.searching_top_bar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.design_system.R
import com.example.design_system.theme.LibertyFlowIcons
import com.example.design_system.theme.LibertyFlowTheme
import com.example.design_system.theme.mTypography

private object SearchingTopBarConstants {
    const val HORIZONTAL_PADDING = 16
    val PlaceholderText = R.string.placeholder_label
}

@Composable
fun SearchingTopBar(
    isLoading: Boolean,
    label: String,
    scrollBehavior: TopAppBarScrollBehavior,
    query: String,
    onQueryChange: (String) -> Unit,
    isSearching: Boolean,
    onSearchChange: () -> Unit
) {
    Column {
        TopAppBar(
            scrollBehavior = scrollBehavior,
            title = {
                // Switch between title text and search text field
                if (isSearching) {
                    SearchingTextField(
                        value = query,
                        onValueChange = onQueryChange
                    )
                } else {
                    Text(
                        text = label,
                        style = mTypography.headlineSmall
                    )
                }
            },
            navigationIcon = {
                // Show back arrow only in search mode
                if (isSearching) {
                    TopBarIconButton(
                        onClick = onSearchChange,
                        icon = LibertyFlowIcons.ArrowLeftFilled
                    )
                }
            },
            actions = {
                // Right side icons depending on search state
                when {
                    // When searching & query has text → show clear button
                    isSearching && query.isNotBlank() -> {
                        TopBarIconButton(
                            onClick = { onQueryChange("") },
                            icon = LibertyFlowIcons.CrossCircle
                        )
                    }
                    // When NOT searching → show search button
                    !isSearching -> {
                        TopBarIconButton(
                            onClick = onSearchChange,
                            icon = LibertyFlowIcons.Magnifier
                        )
                    }
                }
            }
        )

        // Loading indicator appears/disappears with animation
        AnimatedVisibility(
            visible = isLoading,
            enter = fadeIn(tween(300)) + slideInVertically(tween(300)),
            exit = fadeOut(tween(300)) + slideOutVertically(tween(300))
        ) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SearchingTopBarConstants.HORIZONTAL_PADDING.dp)
            )
        }
    }
}

@Composable
private fun SearchingTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = stringResource(SearchingTopBarConstants.PlaceholderText)
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        placeholder = { Text(placeholder) },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
private fun TopBarIconButton(
    onClick: () -> Unit,
    icon: Int
) {
    // Reusable toolbar icon button
    IconButton(onClick = onClick) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null
        )
    }
}

@Preview
@Composable
private fun SearchingTopBarPreview() {
    val scrollBehaviour = TopAppBarDefaults.pinnedScrollBehavior()

    LibertyFlowTheme {
        SearchingTopBar(
            label = "Последние обновления",
            onQueryChange = {},
            query = "",
            scrollBehavior = scrollBehaviour,
            isSearching = false,
            onSearchChange = {},
            isLoading = true
        )
    }
}