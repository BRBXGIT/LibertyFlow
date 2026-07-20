package com.brbx.common.composable.bottom_sheet.collections_sheet

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.brbx.common.model.common.map.toBrbxText
import com.brbx.design_system.component.bottom_sheet.ListSheet
import com.brbx.design_system.component.bottom_sheet.ListSheetItem
import com.brbx.domain.network.model.common.Collection
import com.brbx.ui_compose.common.toBrbxIcon
import dev.chiksmedina.solar.OutlineSolar
import dev.chiksmedina.solar.outline.Arrows
import dev.chiksmedina.solar.outline.arrows.AltArrowRight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
inline fun CollectionsSheet(
    noinline onDismissRequest: () -> Unit,
    crossinline onItemClick: (Collection) -> Unit,
    visible: Boolean,
    modifier: Modifier = Modifier
) =
    ListSheet(
        visible = visible,
        onDismissRequest = onDismissRequest,
        modifier = modifier,
    ) {
        items(Collection.entries) { collection ->
            ListSheetItem(
                icon = OutlineSolar.Arrows.AltArrowRight.toBrbxIcon(),
                text = collection.toBrbxText(),
                modifier = Modifier.fillMaxWidth(),
                onClick = { onItemClick(collection) }
            )
        }
    }