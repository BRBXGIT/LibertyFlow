package com.brbx.domain.network.model.common

import java.time.LocalDateTime

data class DomainYears(
    val fromYear: Int = 0,
    val toYear: Int = LocalDateTime.now().year,
)