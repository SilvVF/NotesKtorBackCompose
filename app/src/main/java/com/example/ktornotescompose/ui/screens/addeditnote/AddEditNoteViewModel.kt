package com.example.ktornotescompose.ui.screens.addeditnote

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ktornotescompose.data.local.entities.Note
import com.example.ktornotescompose.repositories.NoteRepository
import com.example.ktornotescompose.ui.navigation.UiEvent
import com.example.ktornotescompose.ui.navigation.UiText
import com.example.ktornotescompose.ui.screens.notes.components.HexToJetpackColor
import com.example.ktornotescompose.util.Constants.KEY_LOGGED_IN_EMAIL
import com.example.ktornotescompose.util.Constants.NO_EMAIL
import com.example.ktornotescompose.util.Event
import com.example.ktornotescompose.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val repository: NoteRepository,
    private val sharedPref: SharedPreferences
): ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _note = MutableStateFlow<Event<Resource<Note>>?>(null)
    private val note = _note.asStateFlow()

    var state by mutableStateOf(AddEditNoteState())
        private set

    //need the task to happen even if the viewmodel is destroyed
    @OptIn(DelicateCoroutinesApi::class)
    fun insertNote(note: Note) = GlobalScope.launch {
        repository.insertNote(note)
    }

    fun subscribeToNoteUpdates() = viewModelScope.launch {
        note.collect { event ->
                when (event?.content) {
                    is Resource.Success -> {
                        val note = event.content.data
                        note?.let {
                            state = state.copy(
                                titleText = note.title,
                                color = HexToJetpackColor.getColor(note.color),
                                note = note,
                                contentText = note.content,
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiEvent.send(
                            UiEvent.ShowSnackBar(
                                message = UiText.DynamicString(event.content.message ?: "error occurred")
                            )
                        )
                    }
                    else -> {}
                }
        }
    }

    fun getNoteById(id: String) = viewModelScope.launch {
        _note.emit(Event(Resource.Loading(null)))
        val note = repository.getNoteById(id)
        note?.let {
            _note.emit(
                Event(Resource.Success(data = it))
            )
        }  ?: _note.emit(Event(Resource.Error("creating new note")))
    }

    fun onEvent(event: AddEditNoteEvent) {
        when (event) {
            is AddEditNoteEvent.OnColorChangeClick -> {
                state = state.copy(
                    isColorPickerDisplayed = true
                )
            }
            is AddEditNoteEvent.OnDialogDismissed -> {
                state = state.copy(
                    isColorPickerDisplayed = false
                )
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
            is AddEditNoteEvent.OnColorChanged -> {
                state = state.copy(
                    color = event.color
                )
            }
        }
    }
    fun saveNote() {
        //get the current email of the person trying to create a note
        val authEmail = sharedPref.getString(KEY_LOGGED_IN_EMAIL, NO_EMAIL) ?: NO_EMAIL
        val title = state.titleText
        val content = state.contentText
        //validate content of the note is not blank
        if (title.isEmpty() || content.isEmpty()) { return }
        //get required info for saving a note to the backend
        val date = System.currentTimeMillis()
        val color = state.color.toHexCode()
        //generate id if creating a new note
        val id = state.note?.id ?: UUID.randomUUID().toString()
        //generate new list of owners if creating a new note
        val owners = state.note?.owners ?: listOf(authEmail)
        val note = Note(
            title = title,
            date = date,
            content = content,
            color = color,
            id = id,
            owners = owners
        )
        insertNote(note)
    }
    private fun Color.toHexCode(): String {
        val red = this.red * 255
        val green = this.green * 255
        val blue = this.blue * 255
        return String.format("%02x%02x%02x", red.toInt(), green.toInt(), blue.toInt())
    }
}