package com.example.di.data

import android.content.Context
import androidx.work.WorkManager
import com.example.data.local.DataStoreManager
import com.example.data.local.LocalRepository
import com.example.data.remote.RemoteRepository
import com.example.data.remote.service.LampahService
import com.example.data.remote.service.QuranService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Singleton
    @Provides
    @Named("testAja")
    fun provideTestString() = "Testing string dengan hilt"

    @Singleton
    @Provides
    fun provideQuranService(): QuranService {
        return Retrofit.Builder()
            .baseUrl("https://api.alquran.cloud/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QuranService::class.java)
    }

    @Singleton
    @Provides
    fun provideLampahService(): LampahService {
        return Retrofit.Builder()
            .baseUrl("https://lampah-server.vercel.app/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LampahService::class.java)
    }

    @Singleton
    @Provides
    fun provideWorkManager(
        context: Context,
    ): WorkManager {
        return WorkManager.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideDataStoreManager(
        context: Context,
    ): DataStoreManager {
        return DataStoreManager(context)
    }

    @Singleton
    @Provides
    fun provideLocalRepository(
        dataStoreManager: DataStoreManager
    ): LocalRepository {
        return LocalRepository(dataStoreManager)
    }

    @Singleton
    @Provides
    fun provideRemoteRepository(
        quranService: QuranService,
        lampahService: LampahService
    ): RemoteRepository {
        return RemoteRepository(quranService, lampahService)
    }
}
