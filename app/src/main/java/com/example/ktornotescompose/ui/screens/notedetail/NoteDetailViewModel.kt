package com.example.ktornotescompose.ui.screens.notedetail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ktornotescompose.repositories.NoteRepository
import com.example.ktornotescompose.ui.navigation.UiEvent
import com.example.ktornotescompose.ui.navigation.UiText
import com.example.ktornotescompose.util.Event
import com.example.ktornotescompose.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    private val repository: NoteRepository
):ViewModel() {

    var state by mutableStateOf(NoteDetailState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _addOwnerStatus = MutableStateFlow<Event<Resource<String>>?>(null)
    private val addOwnerStatus = _addOwnerStatus.asStateFlow()

    private fun addOwnerToNote() {
        viewModelScope.launch {
            _addOwnerStatus.emit(Event(Resource.Loading()))
            if (state.note.id.isEmpty() || state.ownersText.isEmpty()) {
                _addOwnerStatus.emit(
                    Event(Resource.Error(
                        message = "The owner can't be empty",
                        data = null
                    ))
                )
                return@launch
            }
            val result = repository.addOwnerToNote(state.note.id, state.ownersText)
            when (result) {
                is Resource.Error -> {
                    _addOwnerStatus.emit(
                        Event (
                            Resource.Error(
                                message = result.data?: "an unknown problem occurred",
                            )
                        )
                    )
                }
                is Resource.Success -> {
                    _addOwnerStatus.emit(
                        Event (
                            Resource.Success(
                                data = result.data?: "successfully added owner"
                            )
                        )
                    )
                }
                is Resource.Loading -> {
                    _addOwnerStatus.emit(
                        Event (Resource.Loading())
                    )
                }
            }
        }
    }

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

    fun onEvent(event: NoteDetailEvent) {
        when (event) {
            is NoteDetailEvent.OnAddOwnersIconClick -> {
                state = state.copy(
                    isAddOwnerDialogDisplayed = !state.isAddOwnerDialogDisplayed
                )
            }
            is NoteDetailEvent.UpdateOwnerText -> {
                state = state.copy(
                    ownersText = event.text
                )
            }
            is NoteDetailEvent.OnConfirmOwnersClick -> {
                addOwnerToNote()
            }
        }
    }

    suspend fun subscribeToAddOwnersStatus() {
        addOwnerStatus.collect { event ->
            event?.let {
                val result = event.peekContent()
                when (result) {
                    is Resource.Success -> {
                        _uiEvent.send(
                            UiEvent.ShowSnackBar(
                                UiText.DynamicString(result.data ?: "")
                            )
                        )
                    }
                    is Resource.Loading -> {

                    }
                    is Resource.Error -> {
                        _uiEvent.send(
                            UiEvent.ShowSnackBar(
                                UiText.DynamicString(result.message ?: "error")
                            )
                        )
                    }
                }
            }
        }
    }
}