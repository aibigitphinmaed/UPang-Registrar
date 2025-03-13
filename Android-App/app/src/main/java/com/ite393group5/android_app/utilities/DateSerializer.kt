package com.ite393group5.android_app.utilities

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = LocalDate::class)
object DateSerializer : KSerializer<LocalDate?> {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    override fun serialize(encoder: Encoder, value: LocalDate?) {
        try {
            if (value != null) {
                encoder.encodeString(value.format(formatter))
            } else {
                encoder.encodeNull()
            }
        } catch (e: SerializationException) {
            Timber.tag("DateSerializer").e(e)
        } catch (e: Exception) {
            Timber.tag("DateSerializer").e(e)
        }
    }

    override fun deserialize(decoder: Decoder): LocalDate? {
        return try {
            val dateString = decoder.decodeString()
            if (dateString.isNotBlank()) {
                LocalDate.parse(dateString, formatter)
            } else {
                null
            }
        } catch (e: SerializationException) {
            Timber.tag("DateSerializer").e(e)
            null
        } catch (e: Exception) {
            Timber.tag("DateSerializer").e(e)
            null
        }
    }
}

