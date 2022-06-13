package com.example.ktornotescompose.ui.screens.auth

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.*
import com.example.ktornotescompose.data.remote.BasicAuthInterceptor
import com.example.ktornotescompose.repositories.NoteRepository
import com.example.ktornotescompose.ui.navigation.UiEvent
import com.example.ktornotescompose.ui.navigation.UiText
import com.example.ktornotescompose.util.Constants
import com.example.ktornotescompose.util.Constants.KEY_LOGGED_IN_EMAIL
import com.example.ktornotescompose.util.Constants.KEY_LOGGED_IN_PASSWORD
import com.example.ktornotescompose.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.abs

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val sharedPref: SharedPreferences,
    private val basicAuthInterceptor: BasicAuthInterceptor
): ViewModel() {

    private var currEmail: String? = null
    private var currPassword: String? = null

    var state by mutableStateOf(AuthState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _registerStatus = MutableSharedFlow<Resource<String>>()
    val registerStatus = _registerStatus.asSharedFlow()

    private val _loginStatus = MutableSharedFlow<Resource<String>>()
    val loginStatus = _loginStatus.asSharedFlow()

    suspend fun subscribeToRegisterStatus(): Unit = registerStatus.collect { result ->
         when (result) {
            is Resource.Loading -> {
                state = state.copy(
                    shouldShowProgressbar = true
                )
            }
            is Resource.Success -> {
                delay(500)
                state = state.copy(
                    shouldShowProgressbar = false
                )
                _uiEvent.send(
                    UiEvent.ShowSnackBar(
                        UiText.DynamicString(result.data ?: "Successfully registered an account")
                    )
                )
            }
            is Resource.Error -> {
                delay(500)
                state = state.copy(
                    shouldShowProgressbar = false
                )
                _uiEvent.send(
                    UiEvent.ShowSnackBar(
                        UiText.DynamicString(result.message ?: "An Unknown Error Occurred")
                    )
                )
            }
        }
    }

    suspend fun subscribeToLoginStatus(): Unit = loginStatus.collect { result ->
        when (result) {
            is Resource.Loading -> {
                state = state.copy(
                    shouldShowProgressbar = true
                )
            }
            is Resource.Success -> {
                state = state.copy(
                    shouldShowProgressbar = false
                )
                _uiEvent.send(
                    UiEvent.ShowSnackBar(
                        UiText.DynamicString(result.data ?: "Successfully Logged in to account")
                    )
                )
                sharedPref.edit().putString(KEY_LOGGED_IN_EMAIL, currEmail).apply()
                sharedPref.edit().putString(KEY_LOGGED_IN_PASSWORD, currPassword).apply()
                basicAuthInterceptor.email = currEmail ?: ""
                basicAuthInterceptor.password = currPassword ?: ""
                _uiEvent.send(UiEvent.NavigateUp)
            }
            is Resource.Error -> {
                delay(500)
                state = state.copy(
                    shouldShowProgressbar = false
                )
                _uiEvent.send(
                    UiEvent.ShowSnackBar(
                        UiText.DynamicString(result.message ?: "An Unknown Error Occurred")
                    )
                )
            }
        }
    }

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
                currEmail = state.email
                currPassword = state.password
                login(
                    email = state.email,
                    password = state.password
                )
            }
            is AuthScreenEvent.OnRegisterPasswordChanged -> {
                state = state.copy(
                    passwordRegister = event.password
                )
            }
            is AuthScreenEvent.OnRegisterEmailChanged -> {
                state = state.copy(
                    emailRegister = event.email
                )
            }
            is AuthScreenEvent.OnConfirmPasswordChanged -> {
                state = state.copy(
                    confirmPassword = event.password
                )
            }
            is AuthScreenEvent.OnRegisterButtonClick -> {
                register(
                    email =  state.emailRegister,
                    password = state.passwordRegister,
                    repeatedPassword = state.confirmPassword
                )
            }
            is AuthScreenEvent.Expanded -> {
                state = state.copy(
                    isExpanded = event.isExpanded
                )
            }
        }
    }

    private fun register(email: String, password: String, repeatedPassword: String) = viewModelScope.launch {
        _registerStatus.emit(Resource.Loading())
        if (email.isEmpty() || repeatedPassword.isEmpty() || repeatedPassword.isEmpty()){
            _registerStatus.emit(Resource.Error("Some Fields Are empty", null))
            return@launch
        }
        if (password != repeatedPassword) {
            _registerStatus.emit(Resource.Error("Passwords do not match", null))
            return@launch
        }
        val result = noteRepository.registerUser(email, password)
        _registerStatus.emit(result)
    }
    private fun login(email: String, password: String) = viewModelScope.launch {
        _loginStatus.emit(Resource.Loading())
        if (email.isEmpty() || password.isEmpty()){
            _loginStatus.emit(Resource.Error("Some Fields Are empty", null))
            return@launch
        }
        val result = noteRepository.loginUser(email, password)
        _loginStatus.emit(result)
    }
}