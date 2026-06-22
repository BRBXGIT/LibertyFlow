package com.brbx.preferences.appearance.manager.folder_style

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.brbx.preferences.base.BasePrefsManager
import kotlinx.coroutines.flow.Flow

internal class AppearanceFolderStylePrefsManagerImpl(
    dataStore: DataStore<Preferences>
) : BasePrefsManager(dataStore), AppearanceFolderStylePrefsManager {

    override val folderStyle: Flow<String?> = getValue(FolderStyleKey)

    override suspend fun saveFolderStyle(folderStyle: String) {
        setValue(FolderStyleKey, folderStyle)
    }

    private companion object {
        val FolderStyleKey = stringPreferencesKey(name = "liberty_flow_folder_style")
    }
}