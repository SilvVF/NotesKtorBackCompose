package com.example.ktornotescompose.ui.screens.notedetail

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun AddOwnerBar(
    onAddOwnerIconClick: () -> Unit
) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = {
                onAddOwnerIconClick()
            },
        ) {
            Icon(
                imageVector = Icons.Default.PersonAdd,
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}

@Composable
fun AddOwnerDialog(
    modifier: Modifier = Modifier,
    text: String,
    onDismissRequest: () -> Unit,
    onTextChange: (String) -> Unit,
    onConfirmClick: () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismissRequest() }
    ) {
        Box(modifier = modifier) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(16.dp))
                    .border(
                        width = 1.dp,
                        Color.Cyan,
                        RoundedCornerShape(16.dp)
                    )
                    .padding(4.dp)
            ) {
                TextField(
                    value = text,
                    onValueChange = {
                        onTextChange(it)
                    },
                    textStyle = TextStyle.Default.copy(color = Color.White),
                    placeholder = {
                        Text(text = "Add Owner", color = Color.White)
                    }
                )
                IconButton(
                    onClick = {
                        onDismissRequest()
                        onConfirmClick()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.Green
                    )
                }
            }
        }
    }
}