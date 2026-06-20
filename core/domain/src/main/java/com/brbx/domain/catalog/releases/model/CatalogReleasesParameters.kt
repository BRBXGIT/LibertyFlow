package com.brbx.domain.catalog.releases.model

import com.brbx.domain.model.common.DomainGenre
import com.brbx.domain.model.common.Sorting
import com.brbx.domain.model.common.DomainYears
import com.brbx.domain.model.common.Season

data class CatalogReleasesParameters(
    val isOngoing: Boolean,
    val sorting: Sorting,
    val years: DomainYears,
    val seasons: List<Season>,
    val genres: List<DomainGenre>,
)
