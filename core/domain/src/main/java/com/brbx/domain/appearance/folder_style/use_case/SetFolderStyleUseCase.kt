package com.brbx.domain.appearance.folder_style.use_case

import com.brbx.domain.appearance.folder_style.model.FolderStyle
import com.brbx.domain.appearance.folder_style.repository.FolderStyleRepository

class SetFolderStyleUseCase(
    private val repository: FolderStyleRepository,
) {
    suspend operator fun invoke(style: FolderStyle) {
        repository.set(style)
    }
}