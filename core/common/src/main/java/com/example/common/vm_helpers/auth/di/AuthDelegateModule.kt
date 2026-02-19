package com.example.common.vm_helpers.auth.di

import com.example.common.vm_helpers.auth.delegate.AuthDelegate
import com.example.common.vm_helpers.auth.delegate.AuthDelegateImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * Dagger Hilt module that provides the authentication delegation logic.
 * * This is installed in the [ViewModelComponent], ensuring that the [AuthDelegate]
 * shares the lifecycle of the ViewModel it is injected into.
 */
@Module
@InstallIn(ViewModelComponent::class)
interface AuthDelegateModule {

    @Binds
    @ViewModelScoped
    fun bindAuthDelegate(impl: AuthDelegateImpl): AuthDelegate
}