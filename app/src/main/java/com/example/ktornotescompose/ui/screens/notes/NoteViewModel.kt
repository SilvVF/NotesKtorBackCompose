package com.example.ktornotescompose.ui.screens.notes

import android.content.SharedPreferences
import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.*
import com.example.ktornotescompose.R
import com.example.ktornotescompose.data.local.entities.Note
import com.example.ktornotescompose.repositories.NoteRepository
import com.example.ktornotescompose.ui.navigation.UiEvent
import com.example.ktornotescompose.ui.navigation.UiText
import com.example.ktornotescompose.util.Constants
import com.example.ktornotescompose.util.Event
import com.example.ktornotescompose.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val repository: NoteRepository
): ViewModel() {

    private val _forceUpdate = MutableStateFlow(false)
    @OptIn(ExperimentalCoroutinesApi::class)
    private val _allNotes = _forceUpdate.flatMapLatest {
        repository.getAllNotes()
    }.flatMapLatest {
        MutableStateFlow(Event(it))
    }
    private val allNotes = _allNotes

    var state by mutableStateOf(NoteScreenState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    suspend fun subscribeToNotes() {
        allNotes.collect { event ->
            val result = event.peekContent()
            when (result) {
                is Resource.Loading -> {
                    result.data?.let { notes ->
                        state = state.copy(
                            notesList = notes,
                            isRefreshing = true
                        )
                    }
                }
                is Resource.Success -> {
                    result.data?.let { notes ->
                        state = state.copy(
                            notesList = notes,
                            isRefreshing = false
                        )
                    }
                }
                is Resource.Error -> {
                    event.getContentIfNotHandled()?.let { errorMessage ->
                        errorMessage.message?.let {
                            _uiEvent.send(
                                UiEvent.ShowSnackBar(UiText.DynamicString(it))
                            )
                        }
                    }
                    result.data?.let { notes ->
                        state = state.copy(
                            notesList = notes,
                            isRefreshing = false
                        )
                    }
                }
            }
        }
    }
    fun onEvent(event: NoteScreenEvent) {
        when (event) {
            is NoteScreenEvent.OnFabButtonClick -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.NavigateUp)
                }
            }
            is NoteScreenEvent.OnMenuButtonClick -> {
                state = state.copy(
                    isMenuDisplayed = !state.isMenuDisplayed
                )
            }
            is NoteScreenEvent.OnRefreshTrigger -> {
               viewModelScope.launch {
                   _forceUpdate.emit(true)
               }
            }
            is NoteScreenEvent.OnDismissNoteTrigger -> {
                state = state.copy(
                    notesList = state.notesList.filter { it != event.note }
                )
                viewModelScope.launch {
                    repository.deleteNote(event.note.id)
                    _uiEvent.send(
                        UiEvent.ShowSnackBarWithUndo(
                            UiText.StringResource(R.string.deleted),
                            data = event.note.copy()
                        )
                    )
                }
            }
        }
    }

    fun undoDeletion(note: Note)  {
        viewModelScope.launch {
            repository.insertNote(note)
        }
    }
    fun logout() {
        sharedPreferences.edit()
            .putString(
                Constants.KEY_LOGGED_IN_EMAIL,
                Constants.NO_EMAIL
            ).putString(
                Constants.KEY_LOGGED_IN_PASSWORD,
                Constants.NO_PASSWORD
            ).apply()
    }
}