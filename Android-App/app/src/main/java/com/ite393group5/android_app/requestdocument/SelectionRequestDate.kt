package com.ite393group5.android_app.requestdocument

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ite393group5.android_app.utilities.MySelectableDates
import com.ite393group5.android_app.utilities.convertMillisToLocalDate
import com.ite393group5.android_app.utilities.convertStringDateToMillis


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectionRequestDate(
    paddingValue: PaddingValues,
    screenWidth: Dp,
    screenHeight: Dp,
    selectedDate: (String) -> Unit,
    cancelClick: () -> Unit,
    nextClick: () -> Unit,
    giveToastErrorMessage: (String) -> Unit,
    currentSelectedDate: String
) {


    val dateState = rememberDatePickerState(
        selectableDates = MySelectableDates,
        initialSelectedDateMillis = convertStringDateToMillis(currentSelectedDate)
    )

    Column(
        modifier = Modifier
            .padding(start = 30.dp, end = 30.dp, top = 30.dp, bottom = 30.dp)
            .padding(paddingValue),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
    ) {


        DatePicker(state = dateState, showModeToggle = true)

        Text("Date Selected(MM/dd/yyyy): ${convertMillisToLocalDate(dateState.selectedDateMillis ?: 0L)}")

        Text(
            "2 of 4",
            fontSize = 10.sp
        )

        Row(
            modifier = Modifier
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.Center
        ) {
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
                Text("Back")
            }
            TextButton(
                onClick = {
                    val dateSelectedString =
                        convertMillisToLocalDate(dateState.selectedDateMillis ?: 0L)
                    if (dateSelectedString.isNotEmpty()) {
                        selectedDate.invoke(
                            convertMillisToLocalDate(
                                dateState.selectedDateMillis ?: 0L
                            )
                        )
                        nextClick.invoke()
                    } else {
                        giveToastErrorMessage.invoke("Please select a date")
                    }
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


@Preview(showBackground = true)
@Composable
fun PreviewSelectionRequestDate() {
    SelectionRequestDate(
        paddingValue = PaddingValues(10.dp),
        screenWidth = 300.dp,
        screenHeight = 768.dp,
        selectedDate = { },
        cancelClick = {},
        nextClick = {},
        giveToastErrorMessage = { msg -> },
        currentSelectedDate = ""
    )
}