package com.brbx.network.account_user.auth.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthForm(
    val login: String,
    val password: String,
)
