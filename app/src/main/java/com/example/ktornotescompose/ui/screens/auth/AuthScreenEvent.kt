package com.example.ktornotescompose.ui.screens.auth

sealed class AuthScreenEvent() {
    data class OnEmailChanged(val email: String): AuthScreenEvent()
    data class OnPasswordChanged(val password: String): AuthScreenEvent()
    object OnLoginButtonClick: AuthScreenEvent()
}
