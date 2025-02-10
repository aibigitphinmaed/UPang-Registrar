package com.ite393group5.android_app.services.remote

import com.ite393group5.android_app.models.LocationInfo
import com.ite393group5.android_app.models.LoginRequest
import com.ite393group5.android_app.models.PersonalInfo
import com.ite393group5.android_app.models.Token
import com.ite393group5.android_app.services.local.LocalServiceImpl
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import timber.log.Timber
import javax.inject.Inject

class RemoteServiceImpl @Inject constructor(
    private val localServiceImpl: LocalServiceImpl,
    private val ktorClient: HttpClient
) : RemoteService {
    override suspend fun login(loginRequest: LoginRequest): Boolean {
        try {
            val serverResponse = ktorClient.post("login") {
                contentType(ContentType.Application.Json)
                setBody(loginRequest)
            }
            if (serverResponse.status.value == 200) {
                val token = serverResponse.body<Token>()
                localServiceImpl.saveBearerToken(token)

                return true
            }
            return false
        } catch (e: Exception) {
            Timber.tag("RemoteServiceImpl").e(e)
            return false
        }

    }

    override suspend fun logout() {
        ktorClient.post("logout")
        localServiceImpl.clearEverythingOnLocal()
    }

    override suspend fun refreshToken(): Token {
        val serverResponse =
            ktorClient.post("refreshToken") {

            }
        return try {
            serverResponse.body<Token>()
        } catch (e: Exception) {
            Timber.tag("RemoteServiceImpl").e(e)
            Token("", "", null)
        }
    }

    override suspend fun getPersonalInformation(): PersonalInfo {
        val serverResponse = ktorClient.get("personalInfo")
        return try {
            serverResponse.body<PersonalInfo>()
        } catch (e: Exception) {
            Timber.tag("RemoteServiceImpl").e(e)
            PersonalInfo("", "", "", "", "", "", "", "", "", "")
        }

    }

    override suspend fun getLocationInformation(): LocationInfo {
        val serverResponse = ktorClient.get("locationInfo")
        return try {
            serverResponse.body<LocationInfo>()
        }catch (e:Exception){
            Timber.tag("RemoteServiceImpl").e(e)
            LocationInfo(null,"","","","","","",0)
        }
    }



}