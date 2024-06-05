package com.example.di.domain

import com.example.domain.repository.EditProfileRepository
import com.example.domain.repository.GuestRepository
import com.example.domain.repository.HomeRepository
import com.example.domain.repository.LoginRepository
import com.example.domain.repository.MainRepository
import com.example.domain.repository.RegisterRepository
import com.example.data.local.LocalRepository
import com.example.data.remote.RemoteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class DomainModule {
    @Binds
    abstract fun bindLoginRepository(
        localRepository: LocalRepository
    ): LoginRepository

    @Binds
    abstract fun bindRegisterRepository(
        localRepository: LocalRepository
    ): RegisterRepository

    @Binds
    abstract fun bindHomeRepository(
        localRepository: LocalRepository
    ): HomeRepository

    @Binds
    abstract fun bindEditProfileRepository(
        localRepository: LocalRepository
    ): EditProfileRepository

    @Binds
    abstract fun bindMainRepository(
        remoteRepository: RemoteRepository
    ): MainRepository

    @Binds
    abstract fun bindGuestRepository(
        remoteRepository: RemoteRepository
    ): GuestRepository
}