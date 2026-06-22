package com.brbx.data.appearance.folder_style.repository

import com.brbx.domain.appearance.folder_style.repository.FolderStyleRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val folderStyleRepositoryModule = module {
    singleOf(constructor = ::FolderStyleRepositoryImpl) { bind<FolderStyleRepository>() }
}