package com.example.common.ui_helpers.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

/**
 * Utility function to find the host Activity from a Context.
 */
fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}