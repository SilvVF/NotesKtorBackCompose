package com.example.ktornotescompose.ui.screens.auth

sealed class AuthScreenEvent() {
    data class OnEmailChanged(val email: String): AuthScreenEvent()
    data class OnPasswordChanged(val password: String): AuthScreenEvent()
    object OnLoginButtonClick: AuthScreenEvent()
    data class OnRegisterEmailChanged(val email: String): AuthScreenEvent()
    data class OnRegisterPasswordChanged(val password: String): AuthScreenEvent()
    data class OnConfirmPasswordChanged(val password: String): AuthScreenEvent()
    object OnRegisterButtonClick: AuthScreenEvent()
    data class Expanded(val isExpanded: Boolean): AuthScreenEvent()
}
