package com.healthy.sehatscan.data.remote

import com.healthy.sehatscan.data.remote.auth.response.UserForgetPassword
import com.healthy.sehatscan.data.remote.auth.response.UserLogin
import com.healthy.sehatscan.data.remote.auth.response.UserRegister
import com.healthy.sehatscan.data.remote.disease.response.GetDiseaseResponse
import com.healthy.sehatscan.data.remote.fruit.response.FruitResponse
import com.healthy.sehatscan.data.remote.user.response.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
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

    @POST("api/v1/auth/forgot-password")
    suspend fun forgetPassword(
        @Body requestBody: UserForgetPassword.ForgetPasswordReqBody
    ): Response<UserForgetPassword.ForgetPasswordResponse>

    // User Data
    @GET("api/v1/user")
    suspend fun getUser(
        @Header("Authorization") token: String
    ): Response<UserResponse>

    @GET("api/v1/disease")
    suspend fun getDisease(
        @Header("Authorization") token: String
    ): Response<GetDiseaseResponse>

    @GET("api/v1/fruit")
    suspend fun getFruit(
        @Header("Authorization") token: String
    ): Response<FruitResponse>

}