package com.brbx.domain.model.response.common

import com.brbx.domain.model.common.DomainGenre

data class DomainAnimeItem(
    val favoritesCount: Int,
    val genres: List<DomainGenre>,
    val id: Int,
    val name: DomainName,
    val poster: DomainPoster,
)