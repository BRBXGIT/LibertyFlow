package com.example.collections.screen

import androidx.lifecycle.ViewModel
import com.example.data.domain.CollectionsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CollectionsVM @Inject constructor(
    private val collectionsRepo: CollectionsRepo,
): ViewModel() {

}