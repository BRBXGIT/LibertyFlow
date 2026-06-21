package com.brbx.data.common.map

import com.brbx.domain.model.request.DomainParameters
import com.brbx.domain.model.request.DomainRequest
import com.brbx.network.base.model.request.Parameters
import com.brbx.network.base.model.request.Request

internal fun DomainParameters.Simple.Default.toData(): Parameters.Simple.Default =
    Parameters.Simple.Default(
        ageRatings = this.ageRatings.map { it.toData() },
        search = this.search,
        types = this.types.map { it.toData() },
        years = this.years.joinToString(separator = ","),
        genres = this.genres.joinToString(separator = ","),
        sorting = this.sorting.toData(),
    )

internal fun DomainParameters.Simple.WithoutSorting.toData(): Parameters.Simple.WithoutSorting =
    Parameters.Simple.WithoutSorting(
        ageRatings = this.ageRatings.map { it.toData() },
        search = this.search,
        types = this.types.map { it.toData() },
        years = this.years.joinToString(separator = ","),
        genres = this.genres.joinToString(separator = ","),
    )

internal fun DomainParameters.Complex.toData(): Parameters.Complex =
    Parameters.Complex(
        ageRatings = this.ageRatings.map { it.toData() },
        search = this.search,
        types = this.types.map { it.toData() },
        genres = this.genres.map { it.id },
        publishStatuses = this.publishStatuses.map { it.toData() },
        seasons = this.seasons.map { it.toData() },
        sorting = this.sorting.toData(),
        years = this.years.toData(),
        productionStatuses = this.productionStatuses.map { it.toData() }
    )

internal fun DomainRequest.Simple.toData(): Request.Simple =
    Request.Simple(
        parameters = this.parameters.toData(),
    )

internal fun DomainRequest.Complex.toData(): Request.Complex =
    Request.Complex(
        parameters = this.parameters.toData()
    )