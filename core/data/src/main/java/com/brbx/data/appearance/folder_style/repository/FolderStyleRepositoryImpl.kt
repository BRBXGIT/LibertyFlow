package com.brbx.data.appearance.folder_style.repository

import com.brbx.domain.appearance.folder_style.model.FolderStyle
import com.brbx.domain.appearance.folder_style.repository.FolderStyleRepository
import com.brbx.preferences.appearance.manager.folder_style.AppearanceFolderStylePrefsManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FolderStyleRepositoryImpl(
    private val prefs: AppearanceFolderStylePrefsManager
) : FolderStyleRepository {

    override val value: Flow<FolderStyle> = prefs.value.map {
        it.toFolderStyle()
    }

    override suspend fun set(value: FolderStyle) {
        prefs.save(value.toData())
    }

    internal fun FolderStyle.toData(): String =
        this.name

    internal fun String?.toFolderStyle(): FolderStyle =
        if (this == null) {
            FolderStyle.valueOf(FolderStyle.M3.name)
        } else {
            FolderStyle.valueOf(this)
        }
}