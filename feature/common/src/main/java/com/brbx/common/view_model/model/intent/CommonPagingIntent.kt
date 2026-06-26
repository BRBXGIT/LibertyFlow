package com.brbx.common.view_model.model.intent

sealed interface CommonPagingIntent {
    data object SetUpPaging : CommonPagingIntent

    sealed interface Loading : CommonPagingIntent {
        sealed interface LoadingIntent : Loading {
            @JvmInline value class SetLoading(val loading: Boolean) : LoadingIntent

            @JvmInline value class SetException(val exception: Boolean) : LoadingIntent
        }

        sealed interface RefreshIntent : Loading {
            @JvmInline value class SetRefreshing(val refreshing: Boolean) : RefreshIntent

            @JvmInline value class SetException(val exception: Boolean) : RefreshIntent
        }
    }
}