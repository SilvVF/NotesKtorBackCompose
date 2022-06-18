package com.example.ktornotescompose.ui.screens.notedetail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ktornotescompose.repositories.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    private val repository: NoteRepository
):ViewModel() {

    var state by mutableStateOf(NoteDetailState())
        private set


    fun getNoteFromDb(noteId: String?) = viewModelScope.launch {
        noteId?.let {
            val note = repository.getNoteById(noteId)
            note?.let {
                state = state.copy(
                    note = it
                )
            }
        }
    }

}