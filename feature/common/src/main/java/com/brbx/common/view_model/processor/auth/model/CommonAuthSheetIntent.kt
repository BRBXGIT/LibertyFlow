package com.brbx.common.view_model.processor.auth.model

sealed interface CommonAuthSheetIntent {

    data object ToggleSheet : CommonAuthSheetIntent

    data object Authorize : CommonAuthSheetIntent

    @JvmInline
    value class UpdateLogin(val login: String) : CommonAuthSheetIntent

    @JvmInline
    value class UpdatePassword(val password: String) : CommonAuthSheetIntent
}