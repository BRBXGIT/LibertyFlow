package com.brbx.network.base.model.request

import com.brbx.network.base.common.RequestDefaults
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface Request {
    val exclude: String
    val include: String
    val limit: Int
    val page: Int
    val parameters: Parameters

    @Serializable
    data class Simple(
        override val exclude: String = RequestDefaults.Exclude,
        override val include: String = RequestDefaults.Include,
        override val limit: Int = RequestDefaults.Limit,
        override val page: Int = RequestDefaults.Page,
        @SerialName("f") override val parameters: Parameters.Simple.Default =
            Parameters.Simple.Default(),
    ) : Request

    @Serializable
    data class Collection(
        override val exclude: String = RequestDefaults.Exclude,
        override val include: String = RequestDefaults.Include,
        override val limit: Int = RequestDefaults.Limit,
        override val page: Int = RequestDefaults.Page,
        @SerialName("f") override val parameters: Parameters.Simple.WithoutSorting =
            Parameters.Simple.WithoutSorting(),
        @SerialName("type_of_collection") val collection: String,
    ) : Request

    @Serializable
    data class Complex(
        override val exclude: String = RequestDefaults.Exclude,
        override val include: String = RequestDefaults.Include,
        override val limit: Int = RequestDefaults.Limit,
        override val page: Int = RequestDefaults.Page,
        @SerialName("f") override val parameters: Parameters.Complex =
            Parameters.Complex(),
    ) : Request
}