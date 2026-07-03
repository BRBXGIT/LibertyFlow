package com.brbx.common.view_model.processor.loading.model

sealed interface CommonLoadingIntent {

    @JvmInline value class SetLoading(val loading: Boolean) : CommonLoadingIntent

    @JvmInline value class SetException(val exception: Boolean) : CommonLoadingIntent
}