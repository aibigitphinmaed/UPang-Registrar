package com.ite393group5.android_app.requestdocument

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun SelectionDocument(
    paddingValue: PaddingValues,
    screenWidth: Dp,
    screenHeight: Dp,
    selectedDocument: (String) -> Unit,
    nextClick: () -> Unit,
    cancelClick: () -> Unit,
    currentSelectedType: String
) {
    Column(
        modifier = Modifier
            .padding(start = 30.dp, end = 30.dp)
            .wrapContentSize(align = Alignment.Center),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        ) {
        Text(
            "Select Document", color = Color(0xFF314D36),
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
        )
        Text("UPang Registrar")

        Row(modifier = Modifier.wrapContentSize().weight(2f)){
            ListOfDocuments(
                listOfDocuments = listofDocs,
                selectedDocument = { documentSelected ->
                    selectedDocument.invoke(documentSelected)
                },
                currentSelectedType = currentSelectedType
            )
        }

        Row(modifier = Modifier.weight(0.2f).padding(horizontal = 1.dp, vertical = 10.dp), ){
            Text(
                "1 of 4",
                fontSize = 10.sp
            )
        }

        Row(modifier = Modifier.padding(bottom = 10.dp).weight(0.2f)){
            TextButton(
                onClick = { cancelClick.invoke() },
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
                onClick = { nextClick.invoke() },
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
fun ListOfDocuments(
    listOfDocuments: List<String>,
    selectedDocument: (String) -> Unit,
    currentSelectedType: String
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        userScrollEnabled = true,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .background(color = Color.White, shape = RectangleShape)
            .border(border = BorderStroke(1.dp, Color.Black), RectangleShape)
    ) {
        items(listOfDocuments) { document ->
            ElevatedDocumentCard(
                documentName = document,
                onClick = {
                    selectedDocument.invoke(document)
                },
                currentSelectedType = currentSelectedType
            )
        }
    }
}

@Composable
fun ElevatedDocumentCard(documentName: String, onClick: () -> Unit, currentSelectedType: String) {


    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .sizeIn(minWidth = 100.dp, maxWidth = 200.dp, minHeight = 150.dp, maxHeight = 200.dp)
            .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 10.dp),
        shape = CardDefaults.elevatedShape,
        colors = when (documentName == currentSelectedType) {
            true -> CardDefaults.cardColors(
                containerColor = Color(0xFF314D36),
                contentColor = Color.White
            )

            false -> CardDefaults.cardColors(
                containerColor = Color.White,
                contentColor = Color.Black
            )
        },

        ) {
        TextButton(
            onClick = onClick,

            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 10.dp)
                .fillMaxSize(),
            contentPadding = ButtonDefaults.TextButtonContentPadding,
        ) {
            Text(
                documentName,
                textAlign = TextAlign.Center,
                color = when (documentName == currentSelectedType) {
                    true -> Color.White
                    false -> Color.Black
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSelectionDocument() {
    SelectionDocument(
        paddingValue = PaddingValues(0.dp), 300.dp, 768.dp,
        selectedDocument = { document -> },
        nextClick = {},
        cancelClick = {},
        currentSelectedType = ""
    )
}
