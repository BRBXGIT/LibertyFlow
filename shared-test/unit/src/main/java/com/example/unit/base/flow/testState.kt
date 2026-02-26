package com.example.unit.base.flow

import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.test
import kotlinx.coroutines.flow.Flow

suspend fun <T> Flow<T>.testState(
    skipInitial: Boolean = true,
    validate: suspend ReceiveTurbine<T>.() -> Unit
) {
    this.test {
        if (skipInitial) skipItems(1)
        validate()
        cancelAndIgnoreRemainingEvents()
    }
}