package com.brbx.common.utils

fun <T> Set<T>.toggle(element: T): Set<T> =
    if (contains(element)) this - element else this + element