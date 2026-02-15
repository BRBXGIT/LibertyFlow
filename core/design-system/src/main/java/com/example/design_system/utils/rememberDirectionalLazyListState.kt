package com.example.design_system.utils

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow

/**
 * Represents the direction of a scroll gesture.
 */
enum class ScrollDirection {
    Down, Up
}

/**
 * A state object that tracks the vertical direction of a scrollable container.
 * * It uses a combination of the first visible item index and its scroll offset
 * to determine direction, incorporating a [scrollThreshold] to ignore
 * minor layout jitters.
 */
class DirectionalScrollState {
    var scrollDirection by mutableStateOf(ScrollDirection.Up)
        private set

    private var lastScrollOffset = 0
    private var lastItemIndex = 0

    private val scrollThreshold = 10

    /**
     * Updates the scroll direction based on the current scroll position.
     * @param currentIndex The index of the first visible item.
     * @param currentOffset The scroll offset of the first visible item in pixels.
     */
    fun update(currentIndex: Int, currentOffset: Int) {
        val isScrollingDown = when {
            currentIndex > lastItemIndex -> true
            currentIndex < lastItemIndex -> false
            else -> currentOffset > lastScrollOffset + scrollThreshold
        }

        val isScrollingUp = when {
            currentIndex < lastItemIndex -> true
            currentIndex > lastItemIndex -> false
            else -> currentOffset < lastScrollOffset - scrollThreshold
        }

        if (isScrollingDown) {
            scrollDirection = ScrollDirection.Down
        } else if (isScrollingUp) {
            scrollDirection = ScrollDirection.Up
        }

        lastItemIndex = currentIndex
        lastScrollOffset = currentOffset
    }
}

/**
 * Creates and remembers a [DirectionalScrollState] synced with a [LazyGridState].
 * @param lazyGridState The state of the LazyGrid to monitor.
 * @return A [DirectionalScrollState] that updates reactively as the grid scrolls.
 */
@Composable
fun rememberDirectionalScrollState(lazyGridState: LazyGridState): DirectionalScrollState {
    val scrollState = remember { DirectionalScrollState() }

    LaunchedEffect(lazyGridState) {
        snapshotFlow {
            Pair(lazyGridState.firstVisibleItemIndex, lazyGridState.firstVisibleItemScrollOffset)
        }.collect { (index, offset) ->
                scrollState.update(index, offset)
            }
    }

    return scrollState
}

/**
 * Creates and remembers a [DirectionalScrollState] synced with a [LazyListState].
 * @param lazyListState The state of the LazyList to monitor.
 * @return A [DirectionalScrollState] that updates reactively as the list scrolls.
 */
@Composable
fun rememberDirectionalScrollState(lazyListState: LazyListState): DirectionalScrollState {
    val scrollState = remember { DirectionalScrollState() }

    LaunchedEffect(lazyListState) {
        snapshotFlow {
            Pair(lazyListState.firstVisibleItemIndex, lazyListState.firstVisibleItemScrollOffset)
        }.collect { (index, offset) ->
                scrollState.update(index, offset)
            }
    }

    return scrollState
}