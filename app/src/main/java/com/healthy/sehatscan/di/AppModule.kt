package com.healthy.sehatscan.di

import android.content.Context
import com.healthy.sehatscan.BuildConfig
import com.healthy.sehatscan.appsetting.data.AppSettingDataStore
import com.healthy.sehatscan.appsetting.domain.AppSettingRepository
import com.healthy.sehatscan.data.local.auth.AuthDataStore
import com.healthy.sehatscan.data.remote.ApiService
import com.healthy.sehatscan.data.repository.DrinkHistoryRepository
import com.healthy.sehatscan.data.repository.FavoriteRepository
import com.healthy.sehatscan.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext appContext: Context): Context =
        appContext

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor =
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }
        return OkHttpClient
            .Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://health-api.osc-fr1.scalingo.io/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)


    @Provides
    @Singleton
    fun provideAppSettingDataStore(@ApplicationContext context: Context): AppSettingDataStore =
        AppSettingDataStore(context)

    @Provides
    @Singleton
    fun provideAuthDataStore(@ApplicationContext context: Context): AuthDataStore =
        AuthDataStore(context)

    @Provides
    @Singleton
    fun provideAppSettingRepository(appSettingDataStore: AppSettingDataStore): AppSettingRepository =
        AppSettingRepository(appSettingDataStore)

    @Provides
    @Singleton
    fun provideUserRepository(
        @ApplicationContext context: Context,
        apiService: ApiService,
        authDataStore: AuthDataStore
    ): UserRepository =
        UserRepository(context, apiService, authDataStore)

    @Provides
    @Singleton
    fun provideFavoriteRepository(
        @ApplicationContext context: Context,
        apiService: ApiService,
        authDataStore: AuthDataStore
    ): FavoriteRepository =
        FavoriteRepository(context, apiService, authDataStore)

    @Provides
    @Singleton
    fun provideDrinkHistoryRepository(
        @ApplicationContext context: Context,
        apiService: ApiService,
        authDataStore: AuthDataStore
    ): DrinkHistoryRepository =
        DrinkHistoryRepository(context, apiService, authDataStore)
}