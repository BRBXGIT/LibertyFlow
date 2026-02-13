package com.example.data.di.local

import com.example.data.data.OnboardingRepoImpl
import com.example.data.domain.OnboardingRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface OnboardingModule {

    @Binds
    @Singleton
    fun bindOnboardingRepo(impl: OnboardingRepoImpl): OnboardingRepo
}