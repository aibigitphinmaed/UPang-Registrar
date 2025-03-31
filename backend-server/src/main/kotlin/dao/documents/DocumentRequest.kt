package com.ite393group5.dao.documents

import kotlinx.datetime.LocalDate

data class DocumentRecords(
    val id:Int? = null,
    val studentId:Int,
    val documentType:String,
    val requestedDate: LocalDate,
)

data class DocumentRequirementImage(
    val id: Int,
    val documentId: Int,
    val fileName: String,
    val fileType: String,
)