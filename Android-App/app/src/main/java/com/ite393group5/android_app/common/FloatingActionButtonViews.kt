package com.ite393group5.android_app.common

import android.service.autofill.CustomDescription
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.ModeEdit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun CustomFAB(contentDescription: String, onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        shape = CircleShape,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = Color.White,
        elevation = FloatingActionButtonDefaults.elevation(8.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        if(contentDescription.contains("create",ignoreCase = true)){
        Icon(Icons.Default.Create, contentDescription)
        }else if(contentDescription.contains("modify",ignoreCase = true)){
            Icon(Icons.Default.ModeEdit, contentDescription)
        }

    }
}
