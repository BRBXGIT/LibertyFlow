package com.example.design_system.components.snackbars

suspend fun sendRetrySnackbar(
    label: String,
    action: () -> Unit
) {
    SnackbarController.sendEvent(
        SnackbarEvent(
            message = label,
            action = SnackbarAction(
                name = "Retry",
                action = action
            )
        )
    )
}

suspend fun sendSimpleSnackbar(label: String) = SnackbarController.sendEvent(SnackbarEvent(message = label))