package com.brbx.data.preferences.common.map

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal fun Flow<Boolean?>.booleanNotNull(): Flow<Boolean> = this.map { it == true }