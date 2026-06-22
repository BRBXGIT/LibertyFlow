package com.brbx.domain.appearance.folder_style.use_case

import com.brbx.domain.appearance.folder_style.model.FolderStyle
import com.brbx.domain.appearance.folder_style.repository.FolderStyleRepository
import kotlinx.coroutines.flow.Flow

class GetFolderStyleUseCase(
    private val repository: FolderStyleRepository,
) {
    operator fun invoke(): Flow<FolderStyle> = repository.folderStyle
}