package com.ite393group5.android_app.requestdocument

import android.Manifest.permission.MANAGE_DOCUMENTS
import android.Manifest.permission.MANAGE_EXTERNAL_STORAGE
import android.Manifest.permission.MANAGE_MEDIA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import timber.log.Timber


@Composable
fun UploadRequirementsRequest(
    filesToBeUploaded: (List<Uri?>) -> List<String?>,
    backClick: () -> Unit,
    nextClick: () -> Unit,
    documentType: String,
    currentListOfFiles:List<String?>
) {


    var listOfFiles by remember { mutableStateOf(listOf<String?>()) }
    val requestPermissions =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
            if (results.all {
                    it.value
                }) {
                //Granted here
            } else {

            }
        }

    LaunchedEffect(currentListOfFiles){
        if(currentListOfFiles.isNotEmpty()){
            listOfFiles = currentListOfFiles
        }
    }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) { uri: List<Uri?> ->

            listOfFiles = filesToBeUploaded.invoke(uri)
        }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            requestPermissions.launch(
                arrayOf(
                    READ_MEDIA_IMAGES,
                    READ_MEDIA_VISUAL_USER_SELECTED,
                    READ_EXTERNAL_STORAGE,
                    MANAGE_EXTERNAL_STORAGE,
                    MANAGE_DOCUMENTS,
                    MANAGE_MEDIA
                )
            )
        }
    }




     Column( modifier = Modifier
         .padding(start = 30.dp, end = 30.dp)
         .wrapContentSize(align = Alignment.Center),
         verticalArrangement = Arrangement.Center,
         horizontalAlignment = Alignment.Start,) {
         when (listOfFiles.isNotEmpty()) {
             true -> {
                 Row(modifier = Modifier
                     .wrapContentSize()
                     .weight(2f)){
                     ListOfImagesSelectedView(listOfFiles)
                 }

             }

             false -> {
                 Row(modifier = Modifier
                     .wrapContentSize()
                     .weight(2f),){
                     Text("No Images Selected")
                 }

             }

         }

         Timber.tag("UploadRequirementsRequest").d("Document Type: $documentType")
         val requiredDocuments by remember {
             derivedStateOf {
                 requiredDocumentsMap[documentType] ?: emptyList()
             }
         }
         Text("Kindly upload the following documents required(PNG,JPG,WEBP): ")
         Row(modifier = Modifier
             .padding(bottom = 10.dp)
             .weight(0.3f)){
             Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
                 var x = 0;
                 requiredDocuments.forEach {
                         docReq->
                     x++
                     Text("${x} . ${docReq}")
                 }
             }
         }


         Row(modifier = Modifier
             .padding(bottom = 10.dp)
             .weight(0.3f)){

             Button(onClick = { launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }, modifier = Modifier
                 .weight(1f)
                 .padding(10.dp),
                 border = BorderStroke(1.dp, Color.Black),
                 colors = ButtonColors(
                     containerColor = Color.White,
                     contentColor = Color.Black,
                     disabledContainerColor = Color.White,
                     disabledContentColor = Color.White
                 ),
                 shape = CardDefaults.outlinedShape) {
                 Text(text = "Load Image")
             }
         }

         Row(modifier = Modifier
             .padding(bottom = 10.dp)
             .weight(0.3f)){
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
                 Text("Cancel")
             }
             TextButton(
                 onClick = {
                     nextClick.invoke()
                 },
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
                 Text("Next")
             }


         }


     }




}



@Composable
fun ListOfImagesSelectedView(listOfFiles: List<String?>){
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        userScrollEnabled = true,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.wrapContentSize()
    ) {
        items(listOfFiles) { item ->
            ImageCardCustom(s = item)
        }
    }
}

@Composable
fun ImageCardCustom(s: String?) {
    AsyncImage(
        model = s,
        contentDescription = s,
        modifier = Modifier
            .border(BorderStroke(1.dp, Color.Black), shape = RectangleShape)
            .padding(8.dp)
            .size(width = 200.dp, height = 200.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewUploadRequirementsRequest(){
    UploadRequirementsRequest(filesToBeUploaded = {  listOf<String?>() }, backClick = {}, nextClick = {}, documentType = "Transcript Request", listOf<String>())
}