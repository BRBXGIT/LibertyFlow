package com.brbx.domain

import com.brbx.domain.catalog.catalogModule
import com.brbx.domain.genres.genresModule
import com.brbx.domain.releases.releasesModule
import com.brbx.domain.user.userModule
import org.koin.dsl.module

val domainModule = module {
    includes(
        genresModule,
        catalogModule,
        releasesModule,
        userModule,
    )
}