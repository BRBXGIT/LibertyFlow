package com.brbx.domain.preferences.appearance.folder_style.use_case

import com.brbx.domain.preferences.appearance.folder_style.model.FolderStyle
import com.brbx.domain.preferences.appearance.folder_style.repository.FolderStyleRepository
import kotlinx.coroutines.flow.Flow

class GetAppearanceFolderStyleUseCase(
    private val repository: FolderStyleRepository,
) {
    operator fun invoke(): Flow<FolderStyle> = repository.value
}