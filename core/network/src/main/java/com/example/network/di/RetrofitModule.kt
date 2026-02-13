package com.example.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Hilt module for providing global network-related dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    /**
     * Provides the factory for JSON serialization/deserialization.
     */
    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory =
        GsonConverterFactory.create()

    private const val BASE_URL = "https://aniliberty.top/api/v1/"

    /**
     * Configures and provides the singleton [Retrofit] instance.
     * @param gsonConverterFactory The factory to handle JSON mapping.
     */
    @Provides
    @Singleton
    fun provideRetrofit(gsonConverterFactory: GsonConverterFactory): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }
}