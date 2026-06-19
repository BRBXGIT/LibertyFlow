package com.brbx.network.account_user.auth.model

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class Token(val token: String)