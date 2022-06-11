package com.example.ktornotescompose.ui.screens.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.ktornotescompose.ui.navigation.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(

): ViewModel() {

    var state by mutableStateOf(AuthState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: AuthScreenEvent) {
        when (event) {
            is AuthScreenEvent.OnEmailChanged -> {
                state = state.copy(
                    email = event.email
                )
            }
            is AuthScreenEvent.OnPasswordChanged -> {
                state = state.copy(
                    password = event.password
                )
            }
            is AuthScreenEvent.OnLoginButtonClick -> {

            }
        }
    }
}