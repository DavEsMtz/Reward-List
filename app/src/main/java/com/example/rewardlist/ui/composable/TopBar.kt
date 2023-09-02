package com.example.rewardlist.ui.composable

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.rewardlist.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawTopBar(refresh: () -> Unit) {
    TopAppBar(title = {
        Text(
            text = stringResource(id = R.string.app_name),
            textAlign = TextAlign.Center
        )
    }, actions = {
        IconButton(onClick = { refresh.invoke() }) {
            Icon(imageVector = Icons.Filled.Refresh, contentDescription = "Refresh")
        }
    })
}