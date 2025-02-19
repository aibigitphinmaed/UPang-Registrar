package com.ite393group5.android_app.di

import com.ite393group5.android_app.models.Token
import com.ite393group5.android_app.services.local.LocalService
import com.ite393group5.android_app.services.remote.RemoteService
import com.ite393group5.android_app.services.remote.RemoteServiceImpl
import com.ite393group5.android_app.utilities.Url
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.pingInterval
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import timber.log.Timber
import java.time.Duration
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideKtorClient(
        localServiceImpl: LocalService
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
                url("http://${Url.URL}:${Url.PORT}/")
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                headers{
                    append(HttpHeaders.UserAgent, "Students")
                }
            }
            install(Auth){
                bearer{
                    sendWithoutRequest {
                            request -> request.url.host == "http://${Url.URL}/"
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
                        token.bearerToken?.let {
                            BearerTokens(
                                accessToken = it,
                                refreshToken = token.refreshToken
                            )
                        }
                    }

                }

            }
            install(WebSockets)
            engine {
                preconfigured = OkHttpClient.Builder()
                    .pingInterval(20, TimeUnit.SECONDS)
                    .build()
            }
        }
    }

    @Provides
    @Singleton
    fun provideRemoteService(ktorClient: HttpClient, localService: LocalService): RemoteService {
        return RemoteServiceImpl(localService,ktorClient)
    }



}