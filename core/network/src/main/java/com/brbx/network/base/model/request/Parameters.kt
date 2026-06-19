package com.brbx.network.base.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
sealed interface Parameters {
    val ageRatings: List<String>
    val search: String
    val types: List<String>

    @Serializable
    sealed interface Simple : Parameters {
        val genres: String
        val years: String

        @Serializable
        data class Default(
            @SerialName("age_ratings") override val ageRatings: List<String> = emptyList(),
            override val search: String = "",
            override val types: List<String> = emptyList(),
            override val years: String = "",
            override val genres: String = "",
            val sorting: String = "",
        ) : Simple

        @Serializable
        data class WithoutSorting(
            @SerialName("age_ratings") override val ageRatings: List<String> = emptyList(),
            override val search: String = "",
            override val types: List<String> = emptyList(),
            override val years: String = "",
            override val genres: String = "",
        ) : Simple
    }

    @Serializable
    sealed interface Complex : Parameters {

        @Serializable
        data class Default(
            @SerialName("age_ratings") override val ageRatings: List<String> = emptyList(),
            override val search: String = "",
            override val types: List<String> = emptyList(),
            val genres: List<Int> = emptyList(),
            @SerialName("publish_statuses") val publishStatuses: List<String> = emptyList(),
            val seasons: List<String> = emptyList(),
            val sorting: String = "",
            val years: Years = Years(),
            @SerialName("production_statuses") val productionStatuses: List<String> = emptyList(),
        ) : Complex {

            @Serializable
            data class Years(
                @SerialName("from_year") val fromYear: Int = 0,
                @SerialName("to_year") val toYear: Int = LocalDateTime.now().year,
            )
        }
    }
}