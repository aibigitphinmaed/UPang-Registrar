package com.ite393group5.android_app.requestdocument

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ite393group5.android_app.requestdocument.domain.UserCreateDocumentUseCase
import com.ite393group5.android_app.utilities.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DocumentRequestViewModel @Inject constructor(
    private val networkMonitor: NetworkMonitor,
    private val userCreateDocumentUseCase: UserCreateDocumentUseCase
) : ViewModel()  {

    fun updateSelectedDocument(documentSelected: String) {
            _stateRequestDocumentView.value = _stateRequestDocumentView.value.copy(
                selectedDocument = documentSelected
            )
        Timber.tag("DocumentRequestViewModel").d("updateSelectedDocument: $documentSelected")
    }

    fun goToDateSelection() {
        val documentSelected = _stateRequestDocumentView.value.selectedDocument
        if(!documentSelected.isNullOrBlank()){
            _stateRequestDocumentView.value = _stateRequestDocumentView.value.copy(
                gotoDateSelection = true
            )
        }else{
            viewModelScope.launch {
                _toastMessages.emit("Please select a document to request")
            }
        }
    }

    fun cancelDateSelection() {
        _stateRequestDocumentView.value = _stateRequestDocumentView.value.copy(
            gotoDateSelection = false
        )
    }

    fun updateRequestedDate(selectedDate: String) {
        _stateRequestDocumentView.value = _stateRequestDocumentView.value.copy(
            selectedDate = selectedDate
        )
        Timber.tag("DocumentRequestViewModel").d("updateRequestedDate: $selectedDate")
    }

    fun updateShowWarning(b: Boolean) {
       _stateRequestDocumentView.value = _stateRequestDocumentView.value.copy(
           showWarning = b
       )
    }

    fun giveToastErrorMessage(it: String) {
         viewModelScope.launch {
             _toastMessages.emit(it)
         }
    }

    fun updateIsUserUploadingRequirements(b: Boolean) {
        _stateRequestDocumentView.value = _stateRequestDocumentView.value.copy(
            isUserUploadingRequirements = b
        )
    }

    fun updateFilesToBeUploaded(uris: List<Uri?>): List<String?> {

        val listInString: List<String?> = uris.map {
            it?.toString()
        }
        if(listInString.isNotEmpty()){
            _stateRequestDocumentView.value = _stateRequestDocumentView.value.copy(
                filesToBeUploaded = listInString
            )
            viewModelScope.launch {
                _stateRequestDocumentView.emit(_stateRequestDocumentView.value)
            }
        }else{
            viewModelScope.launch {
                _toastMessages.emit("Please select at least one file to upload")
            }
        }
        return listInString
    }

    fun updateIsUserReviewingRequirements(b: Boolean) {

        if(b){
            if(_stateRequestDocumentView.value.filesToBeUploaded.isEmpty()){
                viewModelScope.launch {
                    _toastMessages.emit("Please upload at least one file to review")
                }
            }else{
                _stateRequestDocumentView.value = _stateRequestDocumentView.value.copy(
                    isUserReviewingRequirements = true
                )
            }
        }else{
            _stateRequestDocumentView.value = _stateRequestDocumentView.value.copy(
                isUserReviewingRequirements = false
            )
        }

    }

    fun updateIsUserSubmitting(b: Boolean) {
        _stateRequestDocumentView.value = _stateRequestDocumentView.value.copy(
            isUserSubmitting = b
        )
    }




    private val _stateRequestDocumentView  = MutableStateFlow(RequestDocumentState())
    val stateRequestDocumentView: StateFlow<RequestDocumentState> = _stateRequestDocumentView

    private val _toastMessages = MutableStateFlow<String?>("")
    val toastMessages: StateFlow<String?> = _toastMessages

    init {
        viewModelScope.launch {
            networkMonitor.isConnected.collect { isOnline ->
                // Handle network connectivity changes
                _stateRequestDocumentView.value = _stateRequestDocumentView.value.copy(hasInternet = isOnline)
            }
        }
    }

    fun startUploadingRequestProcess(context: Context) {
        viewModelScope.launch{
            _stateRequestDocumentView.value = _stateRequestDocumentView.value.copy(
                isUserWaitingForServerResponse = true
            )
            val documentID = userCreateDocumentUseCase.executeCreateDocumentRequest(
                _stateRequestDocumentView.value.selectedDocument,
                _stateRequestDocumentView.value.selectedDate,
            )
            if(documentID != -1 and documentID != null){
                _stateRequestDocumentView.value = _stateRequestDocumentView.value.copy(
                    isDocumentCreatedOnServer = true
                )
              val uploadImagesResponse =  userCreateDocumentUseCase.uploadImageRequirements(
                  context, filesToBeUploaded = _stateRequestDocumentView.value.filesToBeUploaded,
                  docId = documentID
              )
                if(uploadImagesResponse.value == 200){
                    _stateRequestDocumentView.value = RequestDocumentState()
                }
            }
        }
    }

}