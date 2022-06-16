package com.example.ktornotescompose.ui.screens.addeditnote

import androidx.compose.ui.graphics.Color
import com.example.ktornotescompose.data.local.entities.Note

data class AddEditNoteState(
    val note: Note? = null,
    val titleText: String = "",
    val contentText: String = "",
    val color: Color = Color.Cyan
)
