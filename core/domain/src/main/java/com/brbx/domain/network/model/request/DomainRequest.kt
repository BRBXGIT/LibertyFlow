package com.brbx.domain.network.model.request

import com.brbx.domain.network.model.common.DomainCollection

sealed interface DomainRequest {

    val parameters: DomainParameters

    data class Simple(
        override val parameters: DomainParameters.Simple.Default = DomainParameters.Simple.Default(),
    ) : DomainRequest

    data class Collection(
        override val parameters: DomainParameters.Simple.WithoutSorting =
            DomainParameters.Simple.WithoutSorting(),
        val collection: DomainCollection,
    ) : DomainRequest

    data class Complex(
        override val parameters: DomainParameters.Complex = DomainParameters.Complex(),
    ) : DomainRequest
}