package com.ite393group5.android_app.requestdocument

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ite393group5.android_app.common.NoInternetScreen
import com.ite393group5.android_app.utilities.CustomAppTopbar
import com.ite393group5.android_app.utilities.TopBarNavigateBack

val listofDocs = listOf(
    "Transcript Request", "Diploma & Certificate Issuance",
    "Enrollment Verification", "Course Add/Drop & Withdrawal",
    "Student Records Update", "Academic Standing & Graduation Eligibility",
    "Cross-Enrollment & Special Permission Requests",
    "Late Registration & Overload Requests", "Document Authentication & Certification",
)
@Composable
fun DocumentRequestScreen(
    openDrawer: () -> Unit,
    documentRequestViewModel: DocumentRequestViewModel = hiltViewModel<DocumentRequestViewModel>(),
    popToBackStack: () -> Unit
){

    val configuration = LocalConfiguration.current
    val screenwidth = configuration.screenWidthDp.dp
    val screenheight = configuration.screenHeightDp.dp
    val uiState by documentRequestViewModel.stateRequestDocumentView.collectAsState()
    val context = LocalContext.current
    val toastMessages by documentRequestViewModel.toastMessages.collectAsState()
    LaunchedEffect(toastMessages) {
        toastMessages?.let {
            if(it.isNotBlank()){
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    BackHandler(true) {
        when{
            uiState.gotoDateSelection && !uiState.isUserUploadingRequirements -> documentRequestViewModel.cancelDateSelection()
            uiState.isUserUploadingRequirements && !uiState.isUserReviewingRequirements -> documentRequestViewModel.updateIsUserUploadingRequirements(false)
            uiState.isUserReviewingRequirements -> documentRequestViewModel.updateIsUserReviewingRequirements(false)
            uiState.isUserSubmitting -> documentRequestViewModel.updateIsUserSubmitting(false)
            else -> popToBackStack.invoke()
        }
    }


    Scaffold(
        topBar = {
            AnimatedVisibility(!uiState.gotoDateSelection) {
                CustomAppTopbar(
                    title = "Select a Document to Request",
                    openDrawer = openDrawer,
                    modifier = Modifier
                )
            }
            AnimatedVisibility(uiState.gotoDateSelection && !uiState.isUserUploadingRequirements) {
                TopBarNavigateBack(
                    navigateBack = {documentRequestViewModel.cancelDateSelection()},
                    title = "Select a date to request",
                    modifier = Modifier
                )
            }

            AnimatedVisibility(uiState.isUserUploadingRequirements && !uiState.isUserReviewingRequirements) {
                TopBarNavigateBack(
                    navigateBack = {documentRequestViewModel.updateIsUserUploadingRequirements(false)},
                    title = "Upload Requirements",
                    modifier = Modifier
                )
            }
            AnimatedVisibility(uiState.isUserReviewingRequirements && !uiState.isUserSubmitting) {
                TopBarNavigateBack(
                    navigateBack = {documentRequestViewModel.updateIsUserReviewingRequirements(false)},
                    title = "Review Requirements",
                    modifier = Modifier)
            }

            AnimatedVisibility(uiState.isUserSubmitting) {
                TopBarNavigateBack(
                    navigateBack = { documentRequestViewModel.updateIsUserSubmitting(false) },
                    title = "Submitting",
                    modifier = Modifier
                )
            }



        },
        modifier = Modifier
            .fillMaxWidth(1f)
            .fillMaxHeight(1f)
    )
    {
        paddingValue->
        Box(modifier = Modifier
            .padding(paddingValue)
            .background(color = Color.White), contentAlignment = Alignment.TopStart){

            AnimatedVisibility(uiState.hasInternet,
                enter = slideInHorizontally() + expandHorizontally(
                    expandFrom = Alignment.End
                ) + fadeIn(

                    initialAlpha = 0.3f
                ),
                exit = slideOutVertically() + shrinkVertically() + fadeOut()
            ){
                AnimatedVisibility(!uiState.gotoDateSelection) {
                    SelectionDocument(
                        paddingValue,
                        screenwidth,
                        screenheight,
                        selectedDocument = { documentSelected ->
                            documentRequestViewModel.updateSelectedDocument(documentSelected)
                        },
                        nextClick = { documentRequestViewModel.goToDateSelection() },
                        cancelClick = { popToBackStack.invoke() },
                        currentSelectedType = uiState.selectedDocument
                    )
                }
                AnimatedVisibility(uiState.gotoDateSelection && !uiState.isUserUploadingRequirements) {
                    SelectionRequestDate(
                        paddingValue = paddingValue,
                        screenWidth = screenwidth,
                        screenHeight = screenheight,
                        selectedDate = { selectedDate ->
                            documentRequestViewModel.updateRequestedDate(selectedDate)
                        },
                        cancelClick = {
                            documentRequestViewModel.cancelDateSelection()
                        },
                        nextClick = {
                            documentRequestViewModel.updateIsUserUploadingRequirements(true)
                        },
                        giveToastErrorMessage = {
                            documentRequestViewModel.giveToastErrorMessage(it)
                        },

                        currentSelectedDate = uiState.selectedDate
                    )
                }
                AnimatedVisibility(uiState.isUserUploadingRequirements && !uiState.isUserReviewingRequirements) {
                    UploadRequirementsRequest(
                        filesToBeUploaded = {
                            documentRequestViewModel.updateFilesToBeUploaded(it)
                        },
                        backClick = {
                            documentRequestViewModel.updateIsUserUploadingRequirements(false)
                        },
                        nextClick = {
                            documentRequestViewModel.updateIsUserReviewingRequirements(true)
                        },
                        documentType = uiState.selectedDocument,
                        currentListOfFiles = uiState.filesToBeUploaded
                    )
                }
                AnimatedVisibility(uiState.isUserReviewingRequirements) {
                    ReviewDocumentRequest(
                        backClick = {documentRequestViewModel.updateIsUserReviewingRequirements(false)},
                        confirmClick = {},
                        uiState = uiState
                    )
                }






            }
            AnimatedVisibility(!uiState.hasInternet) {
                NoInternetScreen(paddingValue)
            }


        }



    }
}



@Preview(showBackground = true)
@Composable
fun PreviewDocumentRequestScreen() {
    DocumentRequestScreen(
        openDrawer = { },
        popToBackStack = {},
    )
}



