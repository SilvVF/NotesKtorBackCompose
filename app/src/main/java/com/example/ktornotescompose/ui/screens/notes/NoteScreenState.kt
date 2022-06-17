package com.example.ktornotescompose.ui.screens.notes

import com.example.ktornotescompose.data.local.entities.Note
import java.time.LocalDateTime

data class NoteScreenState(
    val isMenuDisplayed: Boolean = false,
    val notesList: List<Note> = emptyList(),
    val isRefreshing: Boolean = false,
)
