package com.ite393group5.models
import kotlinx.serialization.Serializable


@Serializable
data class LocationInfo(
    val houseNumber:String? = null,
    val street:String? = null,
    val zone:String? = null,
    val barangay:String? = null,
    val cityMunicipality:String? = null,
    val province:String? = null,
    val country:String? = null,
    val postalCode:String? = null,
):Updatable
