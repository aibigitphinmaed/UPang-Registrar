package com.ite393group5.android_app.utilities

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.ite393group5.android_app.R
import com.ite393group5.android_app.theme.CustomTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomAppTopbar(title: String, openDrawer: () -> Unit, modifier: Modifier) {
    TopAppBar(
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = modifier.align(Alignment.CenterHorizontally)
                )
            }

        },
        navigationIcon = {
            IconButton(
                onClick = openDrawer){
                Icon(Icons.Filled.Menu, stringResource(id = R.string.open_drawer))
            }

        },
        modifier = Modifier.fillMaxWidth()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarNavigateBack(
    navigateBack: () -> Unit,
    title: String,
    modifier: Modifier,
){
    TopAppBar(
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = modifier.align(Alignment.CenterHorizontally)
                )
            }

        },
        navigationIcon = {
            IconButton(
                onClick = navigateBack){
                Icon(Icons.Filled.ArrowBackIosNew, stringResource(id = R.string.back_button))
            }

        },
        modifier = Modifier.fillMaxWidth()
    )
}


@Preview("Top App Bar")
@Composable
fun PreviewUPangTopAppbar(){
    CustomTheme {
        Column {
            CustomAppTopbar(
                title = "Title TopBar Preview",
                openDrawer = {},
                modifier = Modifier
            )

        }

    }
}