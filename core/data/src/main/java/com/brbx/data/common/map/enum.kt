package com.brbx.data.common.map

internal inline fun <reified T : Enum<T>> String?.toEnumOrDefault(defaultValue: T): T {
    if (this == null) return defaultValue
    return enumValues<T>().firstOrNull { it.name == this } ?: defaultValue
}