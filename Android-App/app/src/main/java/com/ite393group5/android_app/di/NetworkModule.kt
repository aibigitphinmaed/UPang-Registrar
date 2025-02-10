package com.ite393group5.android_app.di

import android.content.Context
import com.ite393group5.android_app.models.Token
import com.ite393group5.android_app.services.local.LocalServiceImpl
import com.ite393group5.android_app.services.remote.RemoteServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideLocalServiceImpl(@ApplicationContext context: Context): LocalServiceImpl {
        return LocalServiceImpl(context)
    }




    @Provides
    @Singleton
    fun provideKtorClient(
        localServiceImpl: LocalServiceImpl
    ): HttpClient {
        return HttpClient(OkHttp){
            install(Logging){
                level = LogLevel.ALL
                logger = object : io.ktor.client.plugins.logging.Logger {
                    override fun log(message: String) {
                        Timber.d("ktor-client", message)
                    }
                }
            }
            install(ContentNegotiation){
                json(
                    Json {
                        prettyPrint = true
                        isLenient = true
                    }
                )
            }
            install(DefaultRequest) {
                url("http://192.168.0.14:8080/")
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
            }

            install(Auth){
                bearer{
                    sendWithoutRequest {
                            request -> request.url.host == "http://192.168.0.14:8080/"
                    }
                    loadTokens {
                        BearerTokens(
                            localServiceImpl.getBearerToken() ,
                            localServiceImpl.getRefreshToken()
                        )
                    }
                    refreshTokens {
                        val token = client.get {
                            markAsRefreshTokenRequest()
                            url("refreshToken")
                            parameter("refreshToken", localServiceImpl.getRefreshToken())
                        }.body<Token>()

                        localServiceImpl.saveRefreshToken(token)
                        BearerTokens(
                            accessToken = token.bearerToken,
                            refreshToken = token.refreshToken
                        )
                    }

                }

            }

        }
    }

    @Provides
    @Singleton
    fun provideRemoteServiceImpl(
        localServiceImpl: LocalServiceImpl,
        ktorClient: HttpClient
    ): RemoteServiceImpl {
        return RemoteServiceImpl(localServiceImpl,ktorClient)
    }

}