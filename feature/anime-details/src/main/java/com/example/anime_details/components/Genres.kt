package com.example.anime_details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.design_system.theme.theme.mColors
import com.example.design_system.theme.theme.mDimens
import com.example.design_system.theme.theme.mTypography

// Lazy item animation key
internal const val GenresLRKey = "GenresLRKey"

/**
 * A horizontally scrollable row of genre tags (chips) for an anime.
 * * This component is designed to be used as an item within a [LazyColumn] or similar
 * parent container using [LazyItemScope].
 * * **Behavior:**
 * - The chips are rendered using [SuggestionChip] but are set to `enabled = false`
 * to serve as static informational tags rather than interactive buttons.
 * - It uses Modifier.animateItem to support smooth transitions when the list
 * content changes or reorders.
 *
 * @param genres A list of strings representing the names of the anime genres (e.g., 'Action', 'Sci-Fi').
 */
@Composable
internal fun LazyItemScope.Genres(
    genres: List<String>
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = mDimens.paddingMedium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(mDimens.spacingSmall),
        modifier = Modifier.animateItem()
    ) {
        // Genre chips
        items(genres) { genre ->
            SuggestionChip(
                colors = SuggestionChipDefaults.suggestionChipColors(
                    disabledLabelColor = mColors.onSurface
                ),
                enabled = false,
                onClick = { /* Do nothing */ },
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