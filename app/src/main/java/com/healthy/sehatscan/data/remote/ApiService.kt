package com.healthy.sehatscan.data.remote

import com.healthy.sehatscan.data.remote.auth.response.UserLogin
import com.healthy.sehatscan.data.remote.auth.response.UserRegister
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    // AUTH
    @POST("api/v1/auth/register")
    suspend fun register(
        @Body requestBody: UserRegister.RegisterRequestBody
    ): Response<UserRegister.RegisterResponse>

    @POST("api/v1/auth/login")
    suspend fun login(
        @Body requestBody: UserLogin.LoginRequestBody
    ): Response<UserLogin.LoginResponse>
}