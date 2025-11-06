package com.example.data.di

import com.example.data.data.GenresRepoImpl
import com.example.data.domain.GenresRepo
import com.example.network.genres.api.GenresApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GenresModule {

    @Provides
    @Singleton
    fun provideGenresApi(retrofit: Retrofit): GenresApi =
        retrofit.create(GenresApi::class.java)

    @Provides
    @Singleton
    fun provideGenresRepo(genresApi: GenresApi): GenresRepo =
        GenresRepoImpl(genresApi)
}