package com.ite393group5.utilities

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = LocalDate::class)
object DateSerializer : KSerializer<LocalDate> {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    override fun serialize(encoder: Encoder, value: LocalDate) {
        try {
            encoder.encodeString(value.format(formatter))
        } catch (e: Exception) {
            throw IllegalArgumentException("Error serializing LocalDate: ${e.message}", e)
        }
    }

    override fun deserialize(decoder: Decoder): LocalDate {
        return try {
            LocalDate.parse(decoder.decodeString(), formatter)
        } catch (e: Exception) {
            throw IllegalArgumentException("Error deserializing LocalDate: ${e.message}", e)
        }
    }
}

