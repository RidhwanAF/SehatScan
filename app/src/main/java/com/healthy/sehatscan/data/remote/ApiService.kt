package com.healthy.sehatscan.data.remote

import com.healthy.sehatscan.data.remote.auth.response.UserForgetPassword
import com.healthy.sehatscan.data.remote.auth.response.UserLogin
import com.healthy.sehatscan.data.remote.auth.response.UserRegister
import com.healthy.sehatscan.data.remote.disease.response.GetDiseaseResponse
import com.healthy.sehatscan.data.remote.drink.response.DrinkRecommendReqBody
import com.healthy.sehatscan.data.remote.drink.response.DrinkResponse
import com.healthy.sehatscan.data.remote.drink.response.FavoriteDrink
import com.healthy.sehatscan.data.remote.drink.response.HistoryDrink
import com.healthy.sehatscan.data.remote.fruit.response.FruitResponse
import com.healthy.sehatscan.data.remote.fruit.response.GetFruitDetailResponse
import com.healthy.sehatscan.data.remote.user.response.UpdateAllergiesResponse
import com.healthy.sehatscan.data.remote.user.response.UpdateDiseaseResponse
import com.healthy.sehatscan.data.remote.user.response.UserProfileData
import com.healthy.sehatscan.data.remote.user.response.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

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

    @PUT("api/v1/user/profile")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body requestBody: UserProfileData.UpdateProfileReqBody
    ): Response<UserProfileData.UpdateProfileResponse>

    @GET("api/v1/disease")
    suspend fun getDisease(
        @Header("Authorization") token: String
    ): Response<GetDiseaseResponse>

    @GET("api/v1/fruit")
    suspend fun getFruit(
        @Header("Authorization") token: String
    ): Response<FruitResponse>

    @POST("api/v1/user/allergy")
    suspend fun updateAllergies(
        @Header("Authorization") token: String,
        @Body requestBody: List<Int>
    ): Response<UpdateAllergiesResponse>

    @POST("api/v1/user/disease")
    suspend fun updateDisease(
        @Header("Authorization") token: String,
        @Body requestBody: List<Int>
    ): Response<UpdateDiseaseResponse>

    // Fruit
    @GET("api/v1/fruit")
    suspend fun getFruitDetail(
        @Header("Authorization") token: String,
        @Query("keyword") keyword: String
    ): Response<GetFruitDetailResponse>

    // Drink
    @POST("api/v1/predict")
    suspend fun getDrinkRecommendation(
        @Header("Authorization") token: String,
        @Body data: DrinkRecommendReqBody
    ): Response<DrinkResponse>

    @GET("api/v1/user/favorite")
    suspend fun getFavoriteDrink(
        @Header("Authorization") token: String
    ): Response<FavoriteDrink.GetFavoriteResponse>

    @POST("api/v1/drink/action")
    suspend fun addRemoveFavorite(
        @Header("Authorization") token: String,
        @Query("type") type: String,
        @Body data: FavoriteDrink.AddRemoveFavoriteReqBody
    ): Response<FavoriteDrink.AddRemoveResponse>

    @GET("api/v1/user/history")
    suspend fun getDrinkHistory(
        @Header("Authorization") token: String
    ): Response<HistoryDrink.DrinkHistoryResponse>
}