package com.example.ktornotescompose.ui.screens.addeditnote

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ktornotescompose.data.local.entities.Note
import com.example.ktornotescompose.repositories.NoteRepository
import com.example.ktornotescompose.repositories.NoteRepositoryImpl
import com.example.ktornotescompose.util.Event
import com.example.ktornotescompose.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val repository: NoteRepository,
): ViewModel() {

    private val _note = MutableStateFlow<Event<Resource<Note>>?>(null)
    private val note = _note.asStateFlow()

    var state by mutableStateOf(AddEditNoteState())
        private set

    //need the task to happen even if the viewmodel is destroyed
    @OptIn(DelicateCoroutinesApi::class)
    fun insertNote(note: Note) = GlobalScope.launch {
        repository.insertNote(note)
    }

    fun getNoteById(id: String) = viewModelScope.launch {
        _note.emit(Event(Resource.Loading(null)))
        val note = repository.getNoteById(id)
        note?.let {
            _note.emit(Event(Resource.Success(it)))
        }  ?: _note.emit(Event(Resource.Error("Note not found")))
    }

    fun onEvent(event: AddEditNoteEvent) {
        when (event) {
            is AddEditNoteEvent.OnColorChangeClick -> {

            }
            is AddEditNoteEvent.OnNoteContentTextChange -> {
                state = state.copy(
                    contentText = event.text
                )
            }
            is AddEditNoteEvent.OnTitleTextChange -> {
                state = state.copy(
                    titleText = event.text
                )
            }
        }
    }
}