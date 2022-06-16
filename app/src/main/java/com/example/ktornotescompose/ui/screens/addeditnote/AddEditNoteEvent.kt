package com.example.ktornotescompose.ui.screens.addeditnote

import androidx.compose.ui.graphics.Color

sealed class AddEditNoteEvent {
    data class OnTitleTextChange(val text: String): AddEditNoteEvent()
    data class OnNoteContentTextChange(val text: String): AddEditNoteEvent()
    object OnColorChangeClick: AddEditNoteEvent()
    object OnDialogDismissed: AddEditNoteEvent()
    data class OnColorChanged(val color: Color): AddEditNoteEvent()
}
