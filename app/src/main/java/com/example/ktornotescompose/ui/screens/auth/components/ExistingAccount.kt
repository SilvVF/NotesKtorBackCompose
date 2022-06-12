package com.example.ktornotescompose.ui.screens.auth.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ktornotescompose.R
import com.example.ktornotescompose.ui.screens.auth.AuthScreenEvent
import com.example.ktornotescompose.ui.screens.auth.AuthViewModel

@Composable
fun ExistingAccount(
    viewModel: AuthViewModel,
    modifier: Modifier = Modifier,
) {
    val state = viewModel.state
    Box(modifier = modifier) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AuthHeader(
                modifier = Modifier
                    .fillMaxWidth(),
                textSize = 26
            )
            LoginInfoBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize()
                    .padding(20.dp),
                email = state.email,
                password = state.password,
                emailChanged = { email ->
                    viewModel.onEvent(AuthScreenEvent.OnEmailChanged(email))
                },
                passwordChanged = { password ->
                    viewModel.onEvent(AuthScreenEvent.OnPasswordChanged(password))
                }
            )
            Button(
                onClick = {
                    viewModel.onEvent(AuthScreenEvent.OnLoginButtonClick)
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Cyan),
                modifier = Modifier
                    .wrapContentSize()
                    .padding(end = 20.dp)
                    .align(
                        alignment = Alignment.End
                    )
            ) {
                Text(text = stringResource(id = R.string.login))
            }
        }
    }
}