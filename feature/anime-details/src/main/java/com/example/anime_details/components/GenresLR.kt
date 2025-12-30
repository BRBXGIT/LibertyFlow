package com.example.anime_details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.design_system.theme.LibertyFlowTheme
import com.example.design_system.theme.mTypography

private const val HORIZONTAL_PADDING = 16
private const val SPACED_BY = 8

internal const val GENRES_LR_KEY = "GENRES_LR_KEY"

@Composable
fun LazyItemScope.GenresLR(
    genres: List<String>
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = HORIZONTAL_PADDING.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(SPACED_BY.dp),
        modifier = Modifier.animateItem()
    ) {
        // TODO ADD LOGIC
        items(genres) { genre ->
            SuggestionChip(
                onClick = {},
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