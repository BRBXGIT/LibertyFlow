package com.example.anime_details.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.design_system.theme.mTypography

// Horizontal padding for the genres row
private const val HORIZONTAL_PADDING = 16

// Spacing between genre chips
private const val SPACED_BY = 8

// Lazy item animation key
internal const val GENRES_LR_KEY = "GENRES_LR_KEY"

@Composable
internal fun LazyItemScope.Genres(
    genres: List<String>
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = HORIZONTAL_PADDING.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(SPACED_BY.dp),
        modifier = Modifier.animateItem()
    ) {
        // Genre chips
        items(genres) { genre ->
            SuggestionChip(
                interactionSource = remember { MutableInteractionSource() },
                onClick = { /* TODO: handle click */ },
                label = {
                    Text(
                        text = genre,
                        style = mTypography.labelLarge
                    )
                }
            )
        }
    }
}

@Preview
@Composable
private fun GenresPreview() {
    LazyColumn {
        item {
            Genres(emptyList())
        }
    }
}