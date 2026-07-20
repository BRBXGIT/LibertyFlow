package com.brbx.design_system.component.bottom_sheet

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.brbx.ui_compose.theme.bShapes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibertyFlowBottomSheet(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    state: SheetState = rememberBottomSheetState(initialValue = SheetValue.Hidden),
    content: @Composable ColumnScope.() -> Unit,
) {
    if (visible) {
        ModalBottomSheet(
            sheetState = state,
            onDismissRequest = onDismissRequest,
            modifier = modifier,
            shape = bShapes.micro4,
            content = content,
        )
    }
}