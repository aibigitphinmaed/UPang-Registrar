package com.ite393group5.android_app.di

import com.ite393group5.android_app.profilemanagement.domain.ProfileChangePasswordUseCase
import com.ite393group5.android_app.profilemanagement.domain.ProfileUpdateUseCase
import com.ite393group5.android_app.profilemanagement.domain.ProfileUseCase
import com.ite393group5.android_app.services.local.LocalService
import com.ite393group5.android_app.services.remote.RemoteService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {


    @Provides
    fun provideProfileUseCase(localService: LocalService, remoteService: RemoteService): ProfileUseCase {
        return ProfileUseCase(localService, remoteService)
    }
    @Provides
    fun provideProfileUpdateUseCase(remoteService: RemoteService,localService: LocalService): ProfileUpdateUseCase {
        return ProfileUpdateUseCase(
            remoteService,
            localService
        )
    }
    @Provides
    fun provideProfileChangePasswordUseCase(remoteService: RemoteService): ProfileChangePasswordUseCase {
        return ProfileChangePasswordUseCase(
            remoteService
        )
    }

}