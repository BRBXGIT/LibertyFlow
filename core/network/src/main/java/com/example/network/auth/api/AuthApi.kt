package com.example.network.auth.api

import com.example.network.auth.models.SessionTokenRequest
import com.example.network.auth.models.SessionTokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("accounts/users/auth/login")
    suspend fun getSessionToken(@Body request: SessionTokenRequest): Response<SessionTokenResponse>
}