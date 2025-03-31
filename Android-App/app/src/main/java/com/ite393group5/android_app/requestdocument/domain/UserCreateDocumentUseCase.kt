package com.ite393group5.android_app.requestdocument.domain

import com.ite393group5.android_app.services.local.LocalService
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserCreateDocumentUseCase @Inject constructor(
    private val localService: LocalService,
    private val httpClient: HttpClient
){
    suspend fun executeCreateDocumentRequest(): HttpStatusCode = withContext(Dispatchers.IO) {
        try{

            HttpStatusCode.OK
        }catch (e:Exception){
            HttpStatusCode.InternalServerError
        }
    }

    suspend fun uploadImageRequirements(): HttpStatusCode = withContext(Dispatchers.IO){
        try{

            HttpStatusCode.OK
        }catch (e: Exception){
            HttpStatusCode.InternalServerError
    }
}