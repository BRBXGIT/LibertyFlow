package com.brbx.domain.common.provider

import kotlinx.coroutines.flow.Flow

interface ValueProvider<T : Any> {

    val value: Flow<T>

    suspend fun set(value: T)
}