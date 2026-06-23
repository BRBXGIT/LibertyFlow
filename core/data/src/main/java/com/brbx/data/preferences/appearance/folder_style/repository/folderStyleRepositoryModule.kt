package com.brbx.data.preferences.appearance.folder_style.repository

import com.brbx.domain.preferences.appearance.folder_style.repository.FolderStyleRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val folderStyleRepositoryModule = module {
    singleOf(constructor = ::FolderStyleRepositoryImpl) { bind<FolderStyleRepository>() }
}