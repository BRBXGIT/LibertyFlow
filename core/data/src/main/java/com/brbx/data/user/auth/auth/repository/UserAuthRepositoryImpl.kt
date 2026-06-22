package com.brbx.data.user.auth.auth.repository

import com.brbx.data.common.map.toDomain
import com.brbx.domain.model.result.DomainRequestResult
import com.brbx.domain.user.auth.model.DomainAuthForm
import com.brbx.domain.user.auth.model.DomainToken
import com.brbx.domain.user.auth.repository.UserAuthRepository
import com.brbx.network.account_user.auth.api.AccountUserAuthApi
import com.brbx.network.account_user.auth.model.AuthForm
import com.brbx.network.account_user.auth.model.Token

internal class UserAuthRepositoryImpl(
    private val api: AccountUserAuthApi
) : UserAuthRepository {

    override suspend fun auth(form: DomainAuthForm): DomainRequestResult<DomainToken> =
        api.auth(authForm = form.toData()).toDomain { it.toDomain() }

    private fun DomainAuthForm.toData(): AuthForm =
        AuthForm(
            login = this.login,
            password = this.password,
        )

    private fun Token.toDomain(): DomainToken =
        DomainToken(token = this.token)
}