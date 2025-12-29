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
import androidx.compose.ui.unit.dp
import com.example.collections.R
import com.example.collections.components.EmptyCollectionConstants.EmptyCollectionLabel
import com.example.design_system.theme.mTypography

private object EmptyCollectionConstants {
    const val HORIZONTAL_PADDING = 16

    val EmptyCollectionLabel = R.string.empty_collection_label
}

@Composable
internal fun EmptyCollectionSection() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = EmptyCollectionConstants.HORIZONTAL_PADDING.dp)
    ) {
        Text(
            text = stringResource(EmptyCollectionLabel),
            style = mTypography.titleMedium,
            textAlign = TextAlign.Center
        )
    }
}
