package com.example.ktornotescompose.ui.screens.notedetail

sealed class NoteDetailEvent {
    data class UpdateOwnerText(val text: String): NoteDetailEvent()
    object OnAddOwnersIconClick: NoteDetailEvent()
    object OnConfirmOwnersClick : NoteDetailEvent()
}
