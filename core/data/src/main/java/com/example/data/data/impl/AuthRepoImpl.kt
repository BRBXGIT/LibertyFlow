package com.example.data.data.impl

import com.example.data.domain.AuthRepo
import com.example.data.models.auth.AuthState
import com.example.data.models.auth.Token
import com.example.data.models.auth.TokenRequest
import com.example.data.utils.remote.network_request.NetworkRequest
import com.example.data.utils.remote.network_request.NetworkResult
import com.example.local.auth.AuthPrefsManager
import com.example.network.auth.api.AuthApi
import com.example.network.auth.models.SessionTokenRequestDto
import com.example.network.auth.models.SessionTokenResponseDto
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of [AuthRepo] that manages user authentication logic.
 *
 * This class serves as a bridge between the remote [AuthApi] and local [AuthPrefsManager],
 * handling token lifecycle, mapping DTOs to domain models, and exposing the current
 * authentication state.
 *
 * @property authApi The network service for authentication requests.
 * @property authPrefsManager The local storage manager for persisting authentication data.
 */
class AuthRepoImpl @Inject constructor(
    private val authApi: AuthApi,
    private val authPrefsManager: AuthPrefsManager
): AuthRepo {

    private companion object {
        const val TOKEN_TYPE = "Bearer"
    }

    /** A Flow that emits the current raw token from local storage. */
    override val token = authPrefsManager.token

    /**
     * A derived Flow representing the user's current [AuthState].
     * Emits [AuthState.LoggedIn] if a valid token exists, otherwise [AuthState.LoggedOut].
     */
    override val authState = token.map { token ->
        if (token.isNullOrBlank()) AuthState.LoggedOut else AuthState.LoggedIn
    }

    /**
     * Authenticates the user and retrieves a new session token.
     *
     * @param request The login credentials (login and password).
     * @return A [NetworkResult] containing the [Token] on success.
     */
    override suspend fun getToken(request: TokenRequest): NetworkResult<Token> {
        return NetworkRequest.safeApiCall(
            call = { authApi.getSessionToken(request.toSessionTokenRequestDto()) },
            map = { it.toToken() }
        )
    }

    /**
     * Terminates the current session on the server.
     * Uses the current [token] as the authorization identifier.
     *
     * @return A [NetworkResult] indicating the outcome of the logout request.
     */
    override suspend fun logout(): NetworkResult<Unit> {
        return NetworkRequest.safeApiCall(
            call = { authApi.logout(token.firstOrNull()) },
            map = {}
        )
    }

    /**
     * Removes the authentication token from local storage.
     */
    override suspend fun clearToken() = authPrefsManager.clearToken()

    /**
     * Persists the provided [token] locally, automatically prepending the [TOKEN_TYPE].
     *
     * @param token The raw token string received from the server.
     */
    override suspend fun saveToken(token: String) = authPrefsManager.saveToken("$TOKEN_TYPE $token")

    // --- Mappers ---
    private fun TokenRequest.toSessionTokenRequestDto(): SessionTokenRequestDto {
        return SessionTokenRequestDto(
            login = login,
            password = password
        )
    }

    private fun SessionTokenResponseDto.toToken(): Token {
        return Token(token = token)
    }
}