package com.example.ktornotescompose.ui.screens.notes.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import com.example.ktornotescompose.data.local.entities.Note


@OptIn(ExperimentalUnitApi::class)
@Composable
fun setUpRow(
    item: Note,
    onClick: (Note) -> Unit
){
    Row(
        modifier= Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        NoteItem(
            note = item,
            onClick = {
                onClick(item)
            }
        )
    }
}