package com.example.ktornotescompose.ui.screens.auth

data class AuthState(
    val email: String = "",
    val password: String = "",
    val motionLayoutProgress: Float = 0f,
    val emailRegister: String = "",
    val passwordRegister: String = "",
    val confirmPassword: String = "",
    val isExpanded: Boolean = false,
    val shouldShowProgressbar: Boolean = false
)
