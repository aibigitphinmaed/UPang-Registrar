package com.ite393group5.android_app.common

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ite393group5.android_app.theme.CustomTheme

@Composable
fun NormalButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
){
    Button(
        modifier = modifier,
        onClick = onClick

    ){
        Text(text = text)
    }
}

@Preview
@Composable
fun PreviewButtons(){
    CustomTheme {
        NormalButton(text = "Hello", onClick = {})
    }
}