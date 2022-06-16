package com.example.ktornotescompose.ui.screens.addeditnote

sealed class AddEditNoteEvent {
    data class OnTitleTextChange(val text: String): AddEditNoteEvent()
    data class OnNoteContentTextChange(val text: String): AddEditNoteEvent()
    object OnColorChangeClick: AddEditNoteEvent()
}
