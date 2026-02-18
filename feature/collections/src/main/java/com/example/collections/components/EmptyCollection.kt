package com.example.collections.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.collections.R
import com.example.design_system.theme.theme.mDimens
import com.example.design_system.theme.theme.mTypography

val EmptyCollectionLabelRes = R.string.empty_collection_label
val EmptyCollectionWithQueryLabelRes = R.string.empty_collection_with_query_label

/**
 * A placeholder UI component displayed when an anime collection contains no items.
 * * * This component centers a text message on the screen, dynamically switching between
 * a general "empty" message and a "no results found" message based on the search state.
 *
 * @param emptyQuery Flag indicating the current search state.
 * If `true`, displays a general empty collection label.
 * If `false` (meaning a query exists), displays a 'no results found' label.
 */
@Composable
internal fun EmptyCollection(emptyQuery: Boolean) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = mDimens.paddingMedium)
    ) {
        Text(
            text = stringResource(if (emptyQuery) EmptyCollectionLabelRes else EmptyCollectionWithQueryLabelRes),
            style = mTypography.titleMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun EmptyCollectionPreview() {
    EmptyCollection(true)
}
