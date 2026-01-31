package com.example.more.screen

import androidx.compose.runtime.Immutable

@Immutable
data class MoreState(
    val isLogoutADVisible: Boolean = false
) {
    fun toggleLogoutAD() = copy(isLogoutADVisible = !isLogoutADVisible)
}
