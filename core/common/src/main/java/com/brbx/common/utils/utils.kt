package com.brbx.common.utils

fun <T> Set<T>.toggle(element: T): Set<T> =
    if (contains(element)) this - element else this + element

fun <T> List<T>.toggle(element: T): List<T> =
    if (contains(element)) this - element else this + element

inline fun <T> getIfOrNull(
    crossinline condition: () -> Boolean,
    crossinline provider: () -> T,
): T? {
    return if (condition()) provider() else null
}