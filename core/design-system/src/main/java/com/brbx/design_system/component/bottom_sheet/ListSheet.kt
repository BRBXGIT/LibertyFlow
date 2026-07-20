package com.brbx.design_system.component.bottom_sheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import com.brbx.ui_compose.common.BrbxIcon
import com.brbx.ui_compose.common.BrbxText
import com.brbx.ui_compose.common.asString
import com.brbx.ui_compose.components.simple.icon.BrbxIcon
import com.brbx.ui_compose.theme.bDimens
import com.brbx.ui_compose.theme.bShapes
import com.brbx.ui_compose.theme.mTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListSheet(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    content: LazyListScope.() -> Unit,
) =
    LibertyFlowBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        visible = visible,
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(bDimens.micro8),
            contentPadding = PaddingValues(all = bDimens.micro8),
            content = content,
        )
    }

@Composable
fun ListSheetItem(
    icon: BrbxIcon,
    text: BrbxText,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) =
    Row(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(all = bDimens.micro4)
            .clip(shape = bShapes.micro4),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = text.asString(),
            style = mTypography.bodyLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f)
                .padding(end = bDimens.micro8)
        )

        BrbxIcon(icon)
    }