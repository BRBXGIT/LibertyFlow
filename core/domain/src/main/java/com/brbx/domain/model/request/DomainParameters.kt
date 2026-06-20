package com.brbx.domain.model.request

import com.brbx.domain.model.common.DomainGenre
import com.brbx.domain.model.common.PublishStatus
import com.brbx.domain.model.common.Season
import com.brbx.domain.model.common.Type
import java.time.LocalDateTime

sealed interface DomainParameters {

    enum class AgeRating {
        R6Plus, R12Plus, R16Plus, R18Plus, R21Plus;
    }

    enum class Sorting {
        CreatedAtDesc, CreatedAtAsc,
        FreshAtDesc, FreshAtAsc,
        RatingDesc, RatingAsc,
        YearDesc, YearAsc;
    }

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
        val years: Years = Years(),
        val productionStatuses: List<ProductionStatus> = emptyList(),
    ) : DomainParameters {

        enum class ProductionStatus {
            InProduction, Finished;
        }

        data class Years(
            val fromYear: Int = 0,
            val toYear: Int = LocalDateTime.now().year,
        )
    }
}