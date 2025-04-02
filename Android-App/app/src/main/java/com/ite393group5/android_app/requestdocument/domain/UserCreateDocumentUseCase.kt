package com.ite393group5.android_app.requestdocument.domain

import android.content.Context
import android.net.Uri
import com.ite393group5.android_app.services.local.LocalService
import io.ktor.client.HttpClient
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import androidx.core.net.toUri
import com.ite393group5.android_app.utilities.getFileBytes
import com.ite393group5.android_app.utilities.getFileName
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.statement.bodyAsText
import io.ktor.http.Headers
import kotlinx.serialization.json.Json

class UserCreateDocumentUseCase @Inject constructor(
    private val localService: LocalService,
    private val httpClient: HttpClient
){
    suspend fun executeCreateDocumentRequest(selectedDocument:String, selectedRequestedDate:String): Int = withContext(Dispatchers.IO) {
        try{
            val token = localService.getBearerToken()
            val response = httpClient.post("student-document-request") {
                contentType(ContentType.Application.Json)
                headers{
                    append(HttpHeaders.Authorization, "Bearer $token" )
                }
                setBody(mapOf("selectedDocument" to selectedDocument, "requestedDate" to selectedRequestedDate))
            }
            // Check if the response is successful and decode the JSON response
            if (response.status == HttpStatusCode.OK) {
                val responseBody = response.bodyAsText()
                val responseMap = Json.decodeFromString<Map<String, Int>>(responseBody)
                return@withContext responseMap["documentId"] ?: -1
            } else {
                // Handle unsuccessful response, returning -1 or a more specific error value
                return@withContext -1
            }
        }catch (e:Exception){
            return@withContext -1
        }
    }

    suspend fun uploadImageRequirements(context: Context, filesToBeUploaded: List<String?>, docId: Int): HttpStatusCode = withContext(Dispatchers.IO) {
        try {

            val token = localService.getBearerToken()
            val listOfImageUris = filesToBeUploaded.mapNotNull {
                it?.toUri()
            }
            val response = httpClient.post("/student-upload-requirement-images"){
                contentType(ContentType.Application.Json)
                headers{
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
                setBody(
                    MultiPartFormDataContent(
                        formData{
                            append("documentId", docId.toString())
                            append("description", "Uploaded from Android User")

                            listOfImageUris.forEach { imageUri ->
                                val fileName = getFileName(context, imageUri)
                                val fileBytes = getFileBytes(context, imageUri)

                                if(fileBytes != null){
                                    append(
                                        key = "documents-uploaded",
                                        value = fileBytes,
                                        headers = Headers.build {
                                            append(HttpHeaders.ContentType, "image/png")
                                            append(HttpHeaders.ContentDisposition, "filename=$fileName")
                                        }
                                    )
                                }
                            }
                        }
                    )
                )
            }
            response.status
        } catch (e: Exception) {
            HttpStatusCode.InternalServerError
        }
    }
}