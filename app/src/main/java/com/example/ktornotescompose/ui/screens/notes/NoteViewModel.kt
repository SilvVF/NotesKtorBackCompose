package com.example.ktornotescompose.ui.screens.notes

import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.ktornotescompose.ui.navigation.UiEvent
import com.example.ktornotescompose.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences
): ViewModel() {

    var state by mutableStateOf(NoteScreenState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

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