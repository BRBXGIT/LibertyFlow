package com.example.data.di

import com.example.data.data.OnboardingRepoImpl
import com.example.data.domain.OnboardingRepo
import com.example.local.onboarding.OnboardingManager
import com.example.local.onboarding.OnboardingManagerImpl
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
    fun bindOnboardingPrefsManager(impl: OnboardingManagerImpl): OnboardingManager

    @Binds
    @Singleton
    fun bindOnboardingRepo(impl: OnboardingRepoImpl): OnboardingRepo
}