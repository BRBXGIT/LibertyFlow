package com.example.network.auth.api

import com.example.network.auth.models.SessionTokenRequestDto
import com.example.network.auth.models.SessionTokenResponseDto
import com.example.network.common.common_utils.CommonNetworkConstants.AUTHORIZATION_HEADER
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Retrofit interface defining authentication endpoints for user accounts.
 */
interface AuthApi {

    /**
     * Authenticates a user and retrieves a session token.
     * @param request The data transfer object containing user credentials.
     * @return A [Response] containing the [SessionTokenResponseDto] if successful.
     */
    @POST(LOGIN_URL)
    suspend fun getSessionToken(
        @Body request: SessionTokenRequestDto
    ): Response<SessionTokenResponseDto>

    /**
     * Terminates the current user session.
     * @param sessionToken The authorization bearer token (can be null).
     * @return A [Response] with no body indicating the result of the logout request.
     */
    @POST(LOGOUT_URL)
    suspend fun logout(
        @Header(AUTHORIZATION_HEADER) sessionToken: String?
    ): Response<Unit>

    companion object Endpoints {
        const val LOGIN_URL = "accounts/users/auth/login"

        const val LOGOUT_URL = "accounts/users/auth/logout"
    }
}