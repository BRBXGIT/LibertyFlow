package com.example.player.player

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun DraggableMiniPlayer() {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        // Определяем границы перемещения (размер экрана минус размер плеера)
        val widthPx = with(LocalDensity.current) { maxWidth.toPx() }
        val heightPx = with(LocalDensity.current) { maxHeight.toPx() }
        val playerWidthPx = with(LocalDensity.current) { 200.dp.toPx() }
        val playerHeightPx = with(LocalDensity.current) { 120.dp.toPx() }

        // Состояние позиции
        var offsetX by remember { mutableStateOf(widthPx - playerWidthPx - 50f) } // Начальная позиция (отступ 50)
        var offsetY by remember { mutableStateOf(heightPx - playerHeightPx - 50f) }

        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                .size(200.dp, 120.dp)
                .background(Color.DarkGray, RoundedCornerShape(12.dp))
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()

                        // Обновляем координаты с ограничением по краям экрана
                        offsetX = (offsetX + dragAmount.x).coerceIn(0f, widthPx - playerWidthPx)
                        offsetY = (offsetY + dragAmount.y).coerceIn(0f, heightPx - playerHeightPx)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            // Твой будущий видеоплеер или заглушка
            Text("Mini Player", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}