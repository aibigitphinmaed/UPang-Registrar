package com.ite393group5.android_app.di

import com.ite393group5.android_app.data.auth.AuthRepository
import com.ite393group5.android_app.data.auth.AuthRepositoryImpl
import com.ite393group5.android_app.services.local.LocalService
import com.ite393group5.android_app.services.local.LocalServiceImpl
import com.ite393group5.android_app.services.remote.RemoteService
import com.ite393group5.android_app.services.remote.RemoteServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository


    @Binds
    @Singleton
    abstract fun bindLocalService(
        localServiceImpl: LocalServiceImpl
    ): LocalService


}