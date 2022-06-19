package com.example.ktornotescompose.ui.screens.notedetail

import com.example.ktornotescompose.data.local.entities.Note

data class NoteDetailState(
    val note: Note = Note(
        title = "",
        content = "",
        date = 0,
        owners = emptyList(),
        color = "000000",
        id = "",
        isSynced = false
    ),
    val isAddOwnerDialogDisplayed: Boolean = false,
    val ownersText: String = ""
)
