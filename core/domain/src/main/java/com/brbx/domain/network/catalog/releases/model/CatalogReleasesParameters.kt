package com.brbx.domain.network.catalog.releases.model

import com.brbx.domain.network.model.common.DomainGenre
import com.brbx.domain.network.model.common.DomainYears
import com.brbx.domain.network.model.common.Season
import com.brbx.domain.network.model.common.Sorting

data class CatalogReleasesParameters(
    val isOngoing: Boolean,
    val sorting: Sorting,
    val years: DomainYears,
    val seasons: List<Season>,
    val genres: List<DomainGenre>,
)
