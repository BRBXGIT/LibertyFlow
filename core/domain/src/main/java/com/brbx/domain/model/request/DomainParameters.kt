package com.brbx.domain.model.request

import com.brbx.domain.model.common.AgeRating
import com.brbx.domain.model.common.DomainGenre
import com.brbx.domain.model.common.ProductionStatus
import com.brbx.domain.model.common.PublishStatus
import com.brbx.domain.model.common.Season
import com.brbx.domain.model.common.Sorting
import com.brbx.domain.model.common.Type
import com.brbx.domain.model.common.DomainYears

sealed interface DomainParameters {

    val ageRatings: List<AgeRating>
    val search: String
    val types: List<Type>

    sealed interface Simple : DomainParameters {
        val genres: List<DomainGenre>
        val years: List<Int>

        data class Default(
            override val ageRatings: List<AgeRating> = emptyList(),
            override val search: String = "",
            override val types: List<Type> = emptyList(),
            override val years: List<Int> = emptyList(),
            override val genres: List<DomainGenre> = emptyList(),
            val sorting: Sorting = Sorting.CreatedAtDesc,
        ) : Simple

        data class WithoutSorting(
            override val ageRatings: List<AgeRating> = emptyList(),
            override val search: String = "",
            override val types: List<Type> = emptyList(),
            override val years: List<Int> = emptyList(),
            override val genres: List<DomainGenre> = emptyList(),
        ) : Simple
    }

    data class Complex(
        override val ageRatings: List<AgeRating> = emptyList(),
        override val search: String = "",
        override val types: List<Type> = emptyList(),
        val genres: List<DomainGenre> = emptyList(),
        val publishStatuses: List<PublishStatus> = emptyList(),
        val seasons: List<Season> = emptyList(),
        val sorting: Sorting = Sorting.CreatedAtDesc,
        val years: DomainYears = DomainYears(),
        val productionStatuses: List<ProductionStatus> = emptyList(),
    ) : DomainParameters
}