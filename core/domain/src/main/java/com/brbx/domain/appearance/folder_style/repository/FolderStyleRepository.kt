package com.brbx.domain.appearance.folder_style.repository

import com.brbx.domain.appearance.folder_style.model.FolderStyle
import kotlinx.coroutines.flow.Flow

interface FolderStyleRepository {

    val folderStyle: Flow<FolderStyle>

    suspend fun setFolderStyle(folderStyle: FolderStyle)
}