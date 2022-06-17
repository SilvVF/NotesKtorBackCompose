package com.example.ktornotescompose.ui.screens.notes

import com.example.ktornotescompose.data.local.entities.Note

sealed class NoteScreenEvent {
    object OnFabButtonClick: NoteScreenEvent()
    object OnMenuButtonClick: NoteScreenEvent()
    object OnRefreshTrigger: NoteScreenEvent()
    data class OnDismissNoteTrigger(val note: Note): NoteScreenEvent()
}
