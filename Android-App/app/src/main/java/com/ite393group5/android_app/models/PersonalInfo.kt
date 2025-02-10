package com.ite393group5.android_app.models

import com.ite393group5.android_app.utilities.DateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class PersonalInfo(
    val firstName: String,
    val lastName:String,
    val middleName:String,
    val extensionName:String? = null,
    val gender:String,
    val citizenship:String,
    val religion:String,
    val civilStatus:String,
    val email:String,
    val number:String,
    @Serializable(with = DateSerializer::class)
    val birthDate: LocalDate,
    val fatherName:String,
    val motherName:String,
    val spouseName:String? = null,
    val contactPersonNumber:String,
)