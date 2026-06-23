package com.brbx.domain.preferences.appearance.folder_style.use_case

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val appearanceFolderStyleUseCaseModule = module {
    factoryOf(constructor = ::GetAppearanceFolderStyleUseCase)
    factoryOf(constructor = ::SetAppearanceFolderStyleUseCase)
}