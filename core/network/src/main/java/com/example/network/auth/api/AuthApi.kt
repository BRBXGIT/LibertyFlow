package com.example.network.auth.api

import com.example.network.auth.models.SessionTokenRequestDto
import com.example.network.auth.models.SessionTokenResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {

    @POST("accounts/users/auth/login")
    suspend fun getSessionToken(@Body request: SessionTokenRequestDto): Response<SessionTokenResponseDto>

    @POST("accounts/users/auth/logout")
    suspend fun logout(@Header("Authorization") sessionToken: String?): Response<Unit>
}