package com.example.ktornotescompose.ui.screens.auth.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.ImeOptions
import androidx.compose.ui.unit.dp

@Composable
fun RegisterInfoBox(
    modifier: Modifier = Modifier,
    email: String = "",
    password: String = "",
    confirmPassword: String = "",
    emailChanged: (String) -> Unit,
    passwordChanged: (String) -> Unit,
    confirmPasswordChanged: (String) -> Unit
) {
    Box (
        modifier = modifier
    ){
        Column {
            TextField(
                value = email,
                onValueChange = {
                    emailChanged(it)
                },
                placeholder = {
                    Text(
                        text = "E-Mail",
                        color = Color.LightGray,
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = password,
                onValueChange = {
                    passwordChanged(it)
                },
                placeholder = {
                    Text(
                        text = "Password",
                        color = Color.LightGray,
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPasswordChanged(it)
                },
                placeholder = {
                    Text(
                        text = "Confirm Password",
                        color = Color.LightGray,
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}