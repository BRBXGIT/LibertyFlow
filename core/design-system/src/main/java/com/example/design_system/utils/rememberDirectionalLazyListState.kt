package com.example.design_system.utils

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
enum class ScrollDirection {
    Down, Up
}

class DirectionalLazyState(
    private val getFirstVisibleItemIndex: () -> Int,
    private val getFirstVisibleItemScrollOffset: () -> Int
) {
    private var positionY = getFirstVisibleItemScrollOffset()
    private var visibleItem = getFirstVisibleItemIndex()

    val scrollDirection by derivedStateOf {
        val currentIndex = getFirstVisibleItemIndex()
        val currentOffset = getFirstVisibleItemScrollOffset()

        val direction = when {
            currentIndex == visibleItem -> {
                if (currentOffset > positionY) ScrollDirection.Down else ScrollDirection.Up
            }
            currentIndex > visibleItem -> ScrollDirection.Down
            else -> ScrollDirection.Up
        }

        positionY = currentOffset
        visibleItem = currentIndex
        direction
    }
}

@Composable
fun rememberDirectionalScrollState(state: LazyListState): DirectionalLazyState {
    return remember(state) {
        DirectionalLazyState(
            getFirstVisibleItemIndex = { state.firstVisibleItemIndex },
            getFirstVisibleItemScrollOffset = { state.firstVisibleItemScrollOffset }
        )
    }
}

@Composable
fun rememberDirectionalScrollState(state: LazyGridState): DirectionalLazyState {
    return remember(state) {
        DirectionalLazyState(
            getFirstVisibleItemIndex = { state.firstVisibleItemIndex },
            getFirstVisibleItemScrollOffset = { state.firstVisibleItemScrollOffset }
        )
    }
}