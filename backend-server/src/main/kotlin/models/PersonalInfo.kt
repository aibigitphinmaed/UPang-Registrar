package com.ite393group5.models

import com.ite393group5.utilities.DateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate


@Serializable
data class PersonalInfo(
    val firstName: String? = null,
    val lastName:String? = null,
    val middleName:String? = null,
    val extensionName:String? = null,
    val gender:String? = null,
    val citizenship:String? = null,
    val religion:String? = null,
    val civilStatus:String? = null,
    val email:String? = null,
    val number:String? = null,
    @Serializable(with = DateSerializer::class)
    val birthDate: LocalDate? = null,
    val fatherName:String? = null,
    val motherName:String? = null,
    val spouseName:String? = null,
    val contactPersonNumber:String? = null,
):Updatable

