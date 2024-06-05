package com.example.di.domain

import androidx.work.WorkManager
import com.example.presentation.usecase.ApplyBlurUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Singleton
    @Provides
    fun provideApplyBlurUseCase(
        workManager: WorkManager
    ): ApplyBlurUseCase {
        return ApplyBlurUseCase(workManager)
    }
}