package com.example.ktornotescompose.ui.screens.notes

sealed class NoteScreenEvent {
    object OnFabButtonClick: NoteScreenEvent()
    object OnMenuButtonClick: NoteScreenEvent()
    object OnRefreshTrigger: NoteScreenEvent()
}
