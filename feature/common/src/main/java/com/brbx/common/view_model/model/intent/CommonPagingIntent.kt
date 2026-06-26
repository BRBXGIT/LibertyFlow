package com.brbx.common.view_model.model.intent

import com.brbx.domain.network.paging.model.PagingException

sealed interface CommonPagingIntent {
    data object SetUpPaging : CommonPagingIntent

    sealed interface Loading : CommonPagingIntent {
        sealed interface LoadingIntent : Loading {
            @JvmInline value class SetLoading(val loading: Boolean) : LoadingIntent

            data class SetException(
                val isException: Boolean,
                val exception: PagingException? = null,
            ) : LoadingIntent
        }

        sealed interface RefreshIntent : Loading {
            @JvmInline value class SetRefreshing(val refreshing: Boolean) : RefreshIntent

            data class SetException(
                val isException: Boolean,
                val exception: PagingException? = null,
            ) : RefreshIntent
        }
    }
}