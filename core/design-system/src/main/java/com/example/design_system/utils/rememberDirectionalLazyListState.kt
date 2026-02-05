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

enum class ScrollDirection {
    Down, Up
}

class DirectionalScrollState {
    var scrollDirection by mutableStateOf(ScrollDirection.Up)
        private set

    private var lastScrollOffset = 0
    private var lastItemIndex = 0

    private val scrollThreshold = 10

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