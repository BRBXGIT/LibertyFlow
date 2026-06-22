package com.brbx.preferences.appearance.manager.folder_style

import kotlinx.coroutines.flow.Flow

interface AppearanceFolderStylePrefsManager {

    val folderStyle: Flow<String?>

    suspend fun saveFolderStyle(folderStyle: String)
}