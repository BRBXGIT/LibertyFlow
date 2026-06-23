package com.brbx.data.preferences.appearance.folder_style.repository

import com.brbx.data.common.map.toEnumOrDefault
import com.brbx.domain.preferences.appearance.folder_style.model.FolderStyle
import com.brbx.domain.preferences.appearance.folder_style.repository.FolderStyleRepository
import com.brbx.preferences.appearance.manager.folder_style.AppearanceFolderStylePrefsManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FolderStyleRepositoryImpl(
    private val prefs: AppearanceFolderStylePrefsManager
) : FolderStyleRepository {

    override val value: Flow<FolderStyle> = prefs.value.map {
        it.toEnumOrDefault(defaultValue = FolderStyle.M3)
    }

    override suspend fun set(value: FolderStyle) {
        prefs.save(value.name)
    }
}