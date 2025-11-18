package com.example.data.models.common.request.request_parameters

import java.time.LocalDateTime

data class UiYear(
    val from: Int = 0,
    val to: Int = LocalDateTime.now().year
)
