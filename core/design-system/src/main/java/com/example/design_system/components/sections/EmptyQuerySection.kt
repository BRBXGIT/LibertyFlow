package com.example.design_system.components.sections

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.design_system.R
import com.example.design_system.theme.mTypography

private object EmptyQuerySectionUtils {
    val emptyQueryLabel = R.string.empty_query_label

    const val HORIZONTAL_PADDING = 16
}

@Composable
fun EmptyQuerySection() {
    Box(
        modifier = Modifier.fillMaxSize().padding(horizontal = EmptyQuerySectionUtils.HORIZONTAL_PADDING.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(EmptyQuerySectionUtils.emptyQueryLabel),
            style = mTypography.titleMedium,
            textAlign = TextAlign.Center
        )
    }
}