package com.brbx.data.network.user.auth.auth.repository

import com.brbx.data.network.api.executor.ApiCallExecutor
import com.brbx.domain.network.model.result.DomainRequestResult
import com.brbx.domain.network.user.auth.model.DomainAuthForm
import com.brbx.domain.network.user.auth.model.DomainToken
import com.brbx.domain.network.user.auth.repository.UserAuthRepository
import com.brbx.network.account_user.auth.api.AccountUserAuthApi
import com.brbx.network.account_user.auth.model.AuthForm
import com.brbx.network.account_user.auth.model.Token

internal class UserAuthRepositoryImpl(
    private val api: AccountUserAuthApi,
    private val executor: ApiCallExecutor,
) : UserAuthRepository {

    override suspend fun auth(form: DomainAuthForm): DomainRequestResult<DomainToken> =
        executor.execute(mapper = { it.toDomain() }) { api.auth(authForm = form.toData()) }

    private fun DomainAuthForm.toData(): AuthForm =
        AuthForm(
            login = this.login,
            password = this.password,
        )

    private fun Token.toDomain(): DomainToken =
        DomainToken(token = this.token)
}