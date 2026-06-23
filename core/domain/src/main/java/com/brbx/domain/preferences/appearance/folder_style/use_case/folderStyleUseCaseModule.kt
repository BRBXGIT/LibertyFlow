package com.brbx.domain.preferences.appearance.folder_style.use_case

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val folderStyleUseCaseModule = module {
    factoryOf(constructor = ::GetFolderStyleUseCase)
    factoryOf(constructor = ::SetFolderStyleUseCase)
}