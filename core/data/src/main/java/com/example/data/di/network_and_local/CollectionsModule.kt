package com.example.data.di.network_and_local

import com.example.data.data.CollectionsRepoImpl
import com.example.data.domain.CollectionsRepo
import com.example.network.collections.api.CollectionsApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface CollectionsModule {

    companion object {
        @Provides
        @Singleton
        fun provideCollectionsApi(retrofit: Retrofit): CollectionsApi =
            retrofit.create(CollectionsApi::class.java)
    }

    @Binds
    @Singleton
    fun bindCollectionsRepo(impl: CollectionsRepoImpl): CollectionsRepo
}