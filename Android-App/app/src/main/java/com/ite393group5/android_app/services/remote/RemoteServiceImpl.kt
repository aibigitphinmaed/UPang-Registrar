package com.ite393group5.android_app.services.remote

import com.ite393group5.android_app.models.LocationInfo
import com.ite393group5.android_app.models.LoginRequest
import com.ite393group5.android_app.models.PersonalInfo
import com.ite393group5.android_app.models.StudentProfile
import com.ite393group5.android_app.models.Token
import com.ite393group5.android_app.models.UserIdResponse
import com.ite393group5.android_app.services.local.LocalService
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class RemoteServiceImpl @Inject constructor(
    private val localServiceImpl: LocalService,
    private val ktorClient: HttpClient
) : RemoteService {
    override suspend fun login(loginRequest: LoginRequest): Boolean = withContext(Dispatchers.IO) {
       return@withContext try {
            val serverResponse = ktorClient.post("login") {
                contentType(ContentType.Application.Json)
                setBody(loginRequest)
            }
            if (serverResponse.status.value == 200) {
                val token = serverResponse.body<Token>()
                localServiceImpl.saveBearerToken(token)
                Timber.tag("RemoteServiceImpl").e(localServiceImpl.getBearerToken())
                true
            }else{
                false
            }
        } catch (e: Exception) {
            Timber.tag("RemoteServiceImpl").e(e)
            false
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

    override suspend fun getPersonalInformation(userId: Int): PersonalInfo = withContext(Dispatchers.IO){
        val token = localServiceImpl.getBearerToken()
        val serverResponse = ktorClient.get("student-profile"){
            contentType(ContentType.Application.Json)
            headers{
                append(HttpHeaders.Authorization, "Bearer $token" )
            }
            setBody(mapOf("userId" to "$userId"))
        }
        return@withContext try {
            val personalInfo = serverResponse.body<PersonalInfo>()
            localServiceImpl.savePersonalInfo(personalInfo)
            Timber.tag("RemoteServiceImpl").e(personalInfo.toString())
            personalInfo
        } catch (e: Exception) {
            Timber.tag("RemoteServiceImpl").e(e)
            PersonalInfo(
                firstName = "", lastName = "", middleName = "",extensionName = null, gender = "", citizenship = "", religion = "", civilStatus =  "", email = "", number = "",
                birthDate = null,
                fatherName = "",
                motherName = "",
                spouseName = "",
                contactPersonNumber = ""
            )
        }

    }

    override suspend fun getLocationInformation(userId: Int): LocationInfo = withContext(Dispatchers.IO){
        val token = localServiceImpl.getBearerToken()
        val serverResponse = ktorClient.get("student-address"){
            contentType(ContentType.Application.Json)
            headers{
                append(HttpHeaders.Authorization, "Bearer $token" )
            }
            setBody(mapOf("userId" to "$userId"))

        }
        return@withContext try {
           val locationInfo = serverResponse.body<LocationInfo>()
            localServiceImpl.saveAddressInfo(locationInfo)
            Timber.tag("RemoteServiceImpl").e(locationInfo.toString())
            locationInfo
        }catch (e:Exception){
            Timber.tag("RemoteServiceImpl").e(e)
            LocationInfo("","","","","","","","")
        }
    }

    override suspend fun retrievePreferences() {
        val token = localServiceImpl.getBearerToken()
        try{
            val serverResponse = ktorClient.post("student-id"){
                contentType(ContentType.Application.Json)
                headers{
                    append(HttpHeaders.Authorization, "Bearer $token" )
                }
            }
            if(serverResponse.status.value == 200) {
                val userId = serverResponse.body<UserIdResponse>()
                localServiceImpl.saveUserId(userId.id)
                val personalInfo = getPersonalInformation(userId.id)
                val locationInfo = getLocationInformation(userId.id)

                localServiceImpl.savePersonalInfo(personalInfo)
                localServiceImpl.saveAddressInfo(locationInfo)
                Timber.tag("RemoteServiceImpl").e(personalInfo.toString())
                Timber.tag("RemoteServiceImpl").e(locationInfo.toString())

                localServiceImpl.saveToDateStore()
            }else{
                Timber.tag("RemoteServiceImpl").e("Failed to retrieve userId in the server")
            }
        }catch (e:Exception){
            Timber.tag("retrievePreferences").e(e)
        }

    }

    override suspend fun updatePersonalInformation(personalInfo: PersonalInfo, locationInfo: LocationInfo): HttpStatusCode = withContext(Dispatchers.IO){
        val token = localServiceImpl.getBearerToken()
        val studentProfile = StudentProfile(personalInfo, locationInfo)
        Timber.tag("RemoteServiceImpl").e("Updating student profile : ${studentProfile.toString()}")
        val serverResponse = ktorClient.post("update-student-profile"){
            contentType(ContentType.Application.Json)
            headers{
                append(HttpHeaders.Authorization, "Bearer $token" )
            }
            setBody(studentProfile)
        }
        return@withContext try {
            serverResponse.status
        }catch (e:Exception){
            Timber.tag("RemoteServiceImpl").e(e, "Error updating student profile")
            HttpStatusCode.InternalServerError
        }

    }



    override suspend fun changePassword(
        currentPassword: String,
        newPassword: String
    ): HttpStatusCode {
        val token = localServiceImpl.getBearerToken()
        val serverResponse = ktorClient.post("student-change-password") {
            contentType(ContentType.Application.Json)
            headers{
                append(HttpHeaders.Authorization, "Bearer $token" )
            }
            setBody(mapOf("currentPassword" to currentPassword, "newPassword" to newPassword))
        }
        return serverResponse.status
    }


}

