package com.healthy.sehatscan.di

import android.content.Context
import com.healthy.sehatscan.appsetting.data.AppSettingDataStore
import com.healthy.sehatscan.appsetting.domain.AppSettingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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
    fun provideAppSettingDataStore(@ApplicationContext context: Context): AppSettingDataStore =
        AppSettingDataStore(context)

    @Provides
    @Singleton
    fun provideAppSettingRepository(appSettingDataStore: AppSettingDataStore): AppSettingRepository =
        AppSettingRepository(appSettingDataStore)

}