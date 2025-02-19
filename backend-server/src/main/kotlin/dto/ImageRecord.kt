package com.ite393group5.dto

import kotlinx.serialization.Serializable

@Serializable
data class ImageRecord(
    val id: Int? = null,
    val fileName: String? = null,
    val fileType: String? = null,
    val userId: Int? = null,
)
