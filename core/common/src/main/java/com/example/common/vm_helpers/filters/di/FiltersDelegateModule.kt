package com.example.common.vm_helpers.filters.di

import com.example.common.vm_helpers.filters.delegate.FiltersDelegate
import com.example.common.vm_helpers.filters.delegate.FiltersDelegateImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * Dagger Hilt module responsible for providing the [FiltersDelegate] implementation.
 * * This module is installed in the [ViewModelComponent], meaning the provided
 * dependencies will live as long as the ViewModel they are injected into.
 */
@Module
@InstallIn(ViewModelComponent::class)
interface FiltersDelegateModule {

    @Binds
    @ViewModelScoped
    fun bindFiltersDelegate(impl: FiltersDelegateImpl): FiltersDelegate
}