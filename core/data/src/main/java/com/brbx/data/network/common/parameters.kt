package com.brbx.data.network.common

import com.brbx.domain.network.model.request.DomainParameters
import com.brbx.domain.network.model.request.DomainRequest
import com.brbx.network.base.model.request.Parameters
import com.brbx.network.base.model.request.Request

internal fun DomainParameters.Simple.Default.toData(): Parameters.Simple.Default =
    Parameters.Simple.Default(
        ageRatings = this.ageRatings.map { it.dataValue },
        search = this.search,
        types = this.types.map { it.dataValue },
        years = this.years.joinToString(separator = ","),
        genres = this.genres.joinToString(separator = ","),
        sorting = this.sorting.dataValue,
    )

internal fun DomainParameters.Simple.WithoutSorting.toData(): Parameters.Simple.WithoutSorting =
    Parameters.Simple.WithoutSorting(
        ageRatings = this.ageRatings.map { it.dataValue },
        search = this.search,
        types = this.types.map { it.dataValue },
        years = this.years.joinToString(separator = ","),
        genres = this.genres.joinToString(separator = ","),
    )

internal fun DomainParameters.Complex.toData(): Parameters.Complex =
    Parameters.Complex(
        ageRatings = this.ageRatings.map { it.dataValue },
        search = this.search,
        types = this.types.map { it.dataValue },
        genres = this.genres.map { it.id },
        publishStatuses = this.publishStatuses.map { it.dataValue },
        seasons = this.seasons.map { it.dataValue },
        sorting = this.sorting.dataValue,
        years = this.years.toData(),
        productionStatuses = this.productionStatuses.map { it.dataValue }
    )

internal fun DomainRequest.Simple.toData(): Request.Simple =
    Request.Simple(
        parameters = this.parameters.toData(),
    )

internal fun DomainRequest.Collection.toData(): Request.Collection =
    Request.Collection(
        parameters = this.parameters.toData(),
        collection = this.collection.dataValue,
    )

internal fun DomainRequest.Complex.toData(): Request.Complex =
    Request.Complex(
        parameters = this.parameters.toData()
    )