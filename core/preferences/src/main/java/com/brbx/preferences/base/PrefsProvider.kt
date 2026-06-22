package com.brbx.preferences.base

import kotlinx.coroutines.flow.Flow

interface PrefsProvider<T> {

    val value: Flow<T?>

    suspend fun save(value: T)
}