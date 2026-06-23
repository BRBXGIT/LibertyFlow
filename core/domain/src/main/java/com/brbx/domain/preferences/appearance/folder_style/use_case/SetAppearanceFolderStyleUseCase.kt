package com.brbx.domain.preferences.appearance.folder_style.use_case

import com.brbx.domain.preferences.appearance.folder_style.model.FolderStyle
import com.brbx.domain.preferences.appearance.folder_style.repository.FolderStyleRepository

class SetAppearanceFolderStyleUseCase(
    private val repository: FolderStyleRepository,
) {
    suspend operator fun invoke(style: FolderStyle) {
        repository.set(style)
    }
}