package com.brbx.domain.model.request

import com.brbx.domain.model.common.DomainCollection

sealed interface DomainRequest {

    val parameters: DomainParameters

    data class Simple(
        override val parameters: DomainParameters = DomainParameters.Simple.Default(),
    ) : DomainRequest

    data class Collection(
        override val parameters: DomainParameters = DomainParameters.Simple.Default(),
        val collection: DomainCollection,
    ) : DomainRequest

    data class Complex(
        override val parameters: DomainParameters = DomainParameters.Complex(),
    ) : DomainRequest
}