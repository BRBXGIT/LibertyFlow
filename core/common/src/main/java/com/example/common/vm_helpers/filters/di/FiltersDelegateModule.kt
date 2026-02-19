package com.example.common.vm_helpers.filters.di

import com.example.common.vm_helpers.filters.delegate.FiltersDelegate
import com.example.common.vm_helpers.filters.delegate.FiltersDelegateImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
interface FiltersDelegateModule {

    @Binds
    @ViewModelScoped
    fun bindFiltersDelegate(impl: FiltersDelegateImpl): FiltersDelegate
}