package com.ite393group5.android_app.models

import kotlinx.serialization.Serializable

@Serializable
data class LocationInfo(
    val houseNumber:String,
    val street:String,
    val zone:String,
    val barangay:String,
    val cityMunicipality:String,
    val province:String,
    val country:String,
    val postalCode:String,
)
