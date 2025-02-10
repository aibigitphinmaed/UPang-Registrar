package com.ite393group5.android_app.utilities

import androidx.datastore.core.Serializer
import com.ite393group5.android_app.models.AppPreferences
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.io.InputStream
import java.io.OutputStream

@Suppress("BlockingMethodInNonBlockingContext")
object AppPreferencesSerializer :Serializer<AppPreferences>{
    override val defaultValue: AppPreferences
        get() = AppPreferences()

    override suspend fun readFrom(input: InputStream): AppPreferences {
       return try{
           Json.decodeFromString(
               deserializer = AppPreferences.serializer(),
               string = input.readBytes().decodeToString()
           )
       }catch(e:SerializationException){
           Timber.tag("AppPreferencesSerializer").e(e)
           defaultValue
       }
    }

    override suspend fun writeTo(t: AppPreferences, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = AppPreferences.serializer(),
                value = t
            ).encodeToByteArray()
        )
    }

}