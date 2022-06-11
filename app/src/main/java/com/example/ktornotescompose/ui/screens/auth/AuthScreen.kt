package com.example.ktornotescompose.ui.screens.auth

import androidx.compose.animation.AnimatedContentScope.SlideDirection.Companion.End
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ktornotescompose.R
import com.example.ktornotescompose.ui.navigation.UiEvent
import com.example.ktornotescompose.ui.screens.auth.components.AuthHeader
import com.example.ktornotescompose.ui.screens.auth.components.LoginInfoBox
import kotlinx.coroutines.flow.collect

@Composable
fun AuthScreen(
    scaffoldState: ScaffoldState,
    viewModel: AuthViewModel = hiltViewModel()
) {
    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val state = viewModel.state
        val context = LocalContext.current
        AuthHeader(
            modifier = Modifier.fillMaxWidth(),
            textSize = 26
        )
        LaunchedEffect(key1 = true) {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is UiEvent.ShowSnackBar -> {
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = event.message.asString(context)
                        )
                    }
                    else -> {}
                }
            }
        }
        LoginInfoBox(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize()
                .padding(20.dp),
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
            modifier = Modifier.align(
                alignment = Alignment.End
            )
            .padding(end = 20.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Cyan)
        ) {
            Text(text = stringResource(id = R.string.login))
        }
    }
}