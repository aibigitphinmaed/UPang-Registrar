package com.ite393group5.android_app.requestdocument

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import timber.log.Timber


val requiredDocumentsMap = mapOf(
    "Transcript Request" to listOf("Valid Student ID", "Payment Receipt"),
    "Diploma & Certificate Issuance" to listOf("Graduation Clearance", "Valid Student ID"),
    "Enrollment Verification" to listOf("Valid Student ID", "Proof of Enrollment"),
    "Course Add/Drop & Withdrawal" to listOf("Completed Add/Drop Form", "Adviser Approval", "Withdrawal Request Form", "Dean's Approval"),
    "Student Records Update" to listOf("Request Form", "Proof of Change (e.g., Birth Certificate, Government ID)"),
    "Academic Standing & Graduation Eligibility" to listOf("Academic Records", "Graduation Checklist", "Dean's Approval"),
    "Cross-Enrollment & Special Permission Requests" to listOf("Cross-Enrollment Approval", "Valid Student ID", "Request Form", "Department Approval"),
    "Late Registration & Overload Requests" to listOf("Late Registration Approval", "Valid Student ID", "Academic Standing Proof", "Adviser Approval"),
    "Document Authentication & Certification" to listOf("Original Documents", "Authentication Request Form", "Valid Student ID")
)


@Composable
fun ReviewDocumentRequest(
    backClick: () -> Unit,
    confirmClick: () -> Unit,
    uiState: RequestDocumentState
){

    Timber.tag("ReviewDocumentRequest").e(uiState.toString())

    var listOfFiles by remember { mutableStateOf(listOf<String?>()) }

    LaunchedEffect(uiState){
        listOfFiles = uiState.filesToBeUploaded
    }

    Column(modifier = Modifier.padding(start = 30.dp, end = 30.dp, top = 30.dp).wrapContentSize(align = Alignment.Center), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start) {

        Row(modifier = Modifier.weight(0.05f).fillMaxWidth()){
           Text("Review Request", fontSize = 30.sp, color = Color(0xFF314D36), fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth())
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
            Card(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.8f).padding(10.dp).border(border = BorderStroke(1.dp, Color.Black)), shape = RectangleShape, colors = CardColors(
                containerColor = Color.Transparent,
                contentColor = Color.Black,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = Color.Black
            )) {
                Text("Document Type:", modifier = Modifier.padding(10.dp))
                Text(uiState.selectedDocument, modifier = Modifier.padding(top = 5.dp, bottom = 10.dp, start = 30.dp, end = 10.dp))
                Text("Requested Date:",modifier = Modifier.padding(10.dp))
                Text(uiState.selectedDate, modifier = Modifier.padding(top = 5.dp, bottom = 10.dp, start = 30.dp, end = 10.dp))
                Text("Files To Be Uploaded:",modifier = Modifier.padding(10.dp))
                ListOfImagesSelectedView(listOfFiles)
            }
        }

        Row{
            Text("4 of 4")
        }


        Row(modifier = Modifier.padding(top = 10.dp,bottom = 10.dp).weight(0.2f)){
            TextButton(
                onClick = { backClick.invoke() },
                modifier = Modifier
                    .weight(1f)
                    .padding(10.dp),
                border = BorderStroke(1.dp, Color.Black),
                colors = ButtonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black,
                    disabledContainerColor = Color.White,
                    disabledContentColor = Color.White
                ),
                shape = CardDefaults.outlinedShape
            ) {
                Text("Back")
            }
            TextButton(
                onClick = { confirmClick.invoke() },
                modifier = Modifier
                    .weight(1f)
                    .padding(10.dp),
                border = BorderStroke(1.dp, Color.Black),
                colors = ButtonColors(
                    containerColor = Color(0xFF314D36),
                    contentColor = Color.White,
                    disabledContainerColor = Color.White,
                    disabledContentColor = Color.White
                ),
                shape = CardDefaults.outlinedShape
            ) {
                Text("Confirm")
            }


        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewReviewDocumentRequest(){
    ReviewDocumentRequest({},{},RequestDocumentState())
}