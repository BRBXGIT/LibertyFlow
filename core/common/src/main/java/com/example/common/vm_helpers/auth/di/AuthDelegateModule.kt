package com.example.common.vm_helpers.auth.di

import com.example.common.vm_helpers.auth.delegate.AuthDelegate
import com.example.common.vm_helpers.auth.delegate.AuthDelegateImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
interface AuthDelegateModule {

    @Binds
    @ViewModelScoped
    fun bindAuthDelegate(impl: AuthDelegateImpl): AuthDelegate
}