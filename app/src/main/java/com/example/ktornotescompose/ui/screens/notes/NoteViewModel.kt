package com.example.ktornotescompose.ui.screens.notes

import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.ktornotescompose.repositories.NoteRepository
import com.example.ktornotescompose.ui.navigation.UiEvent
import com.example.ktornotescompose.ui.navigation.UiText
import com.example.ktornotescompose.util.Constants
import com.example.ktornotescompose.util.Event
import com.example.ktornotescompose.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val repository: NoteRepository
): ViewModel() {

    var state by mutableStateOf(NoteScreenState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _forceUpdate = MutableStateFlow(false)
    @OptIn(ExperimentalCoroutinesApi::class)
    private val _allNotes = _forceUpdate.flatMapLatest {
        repository.getAllNotes().flatMapLatest {
            MutableStateFlow(Event(it))
        }
    }
    val allNotes = _allNotes

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
                    event.content.message?.let { errorMessage ->
                        _uiEvent.send(
                            UiEvent.ShowSnackBar(UiText.DynamicString(errorMessage))
                        )
                    }
                    result.data?.let { notes ->
                        state = state.copy(
                            notesList = notes
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: NoteScreenEvent) {
        when (event) {
            is NoteScreenEvent.OnFabButtonClick -> {

            }
            is NoteScreenEvent.OnMenuButtonClick -> {
                state = state.copy(
                    isMenuDisplayed = !state.isMenuDisplayed
                )
            }
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