package com.ite393group5.android_app.requestdocument


data class RequestDocumentState (
    val selectedDocument: String = "",

    val gotoDateSelection:Boolean = false,
    val selectedDate: String = "",

    val isUserUploadingRequirements:Boolean = false,
    val filesToBeUploaded: List<String?> = emptyList(),


    val isUserReviewingRequirements: Boolean = false,

    val isUserSubmitting: Boolean = false,

    val hasInternet: Boolean = false,
    val showWarning: Boolean = false,
)


//selected document
//
//selected date
//files uploaded
//first confirmation
//final confirmation