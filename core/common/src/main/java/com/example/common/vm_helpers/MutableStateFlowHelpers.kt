package com.example.common.vm_helpers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

context(viewModel: ViewModel)
fun <T> Flow<T>.toEagerly(initialState: T): StateFlow<T> {
    return this.stateIn(
        viewModel.viewModelScope,
        SharingStarted.Eagerly,
        initialState
    )
}

context(viewModel: ViewModel)
fun <T> Flow<T>.toLazily(initialState: T): StateFlow<T> {
    return this.stateIn(
        viewModel.viewModelScope,
        SharingStarted.Lazily,
        initialState
    )
}

context(viewModel: ViewModel)
fun <T> Flow<T>.toWhileSubscribed(initialState: T): StateFlow<T> {
    return this.stateIn(
        viewModel.viewModelScope,
        SharingStarted.WhileSubscribed(1),
        initialState
    )
}