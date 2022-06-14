package com.example.ktornotescompose.ui.screens.auth


import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ktornotescompose.ui.navigation.UiEvent
import com.example.ktornotescompose.ui.screens.auth.components.ExistingAccount
import com.example.ktornotescompose.ui.screens.auth.components.RegisterContainer

@Composable
fun AuthScreen(
    scaffoldState: ScaffoldState,
    viewModel: AuthViewModel = hiltViewModel(),
    OnSuccessfulLogin: () -> Unit
) {
    val state = viewModel.state
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.subscribeToRegisterStatus()
    }
    LaunchedEffect(key1 = true) {
        viewModel.subscribeToLoginStatus()
    }
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message.asString(context)
                    )
                }
                is UiEvent.NavigateUp -> OnSuccessfulLogin()
                else -> {

                }
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (state.shouldShowProgressbar) {
            CircularProgressIndicator(
                modifier = Modifier.fillMaxSize(0.2f),
            )
        }
        if (!state.isExpanded){
            ExistingAccount(
                viewModel = viewModel,
                modifier = Modifier
                    .animateContentSize(
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = LinearEasing
                        ),
                    )
                    .fillMaxHeight(0.8f)
                    .fillMaxWidth()
            )
        }
        RegisterContainer(
            modifier = when (state.isExpanded) {
                false -> {
                    Modifier
                        .animateContentSize(
                            animationSpec = tween(
                                durationMillis = 300,
                                easing = LinearEasing
                            ),
                        )
                        .fillMaxWidth()
                        .fillMaxHeight(0.4f)
                        .background(Color.LightGray)
                }
                true -> {
                    Modifier
                        .fillMaxSize()
                        .animateContentSize(
                            animationSpec = tween(
                                durationMillis = 300,
                                easing = FastOutLinearInEasing
                            ),
                        )
                        .background(Color.DarkGray)
                }
            },
            email = state.emailRegister,
            password = state.passwordRegister,
            confirmPassword = state.confirmPassword,
            isExpanded = state.isExpanded,
            emailChanged = {
                viewModel.onEvent(AuthScreenEvent.OnRegisterEmailChanged(it))
            },
            passwordChanged = {
                viewModel.onEvent(AuthScreenEvent.OnRegisterPasswordChanged(it))
            },
            confirmPasswordChanged = {
                viewModel.onEvent(AuthScreenEvent.OnConfirmPasswordChanged(it))
            },
            onRegisterClick = {
                viewModel.onEvent(AuthScreenEvent.OnRegisterButtonClick)
            },
            expandClicked = {
                viewModel.onEvent(AuthScreenEvent.Expanded(it))
            }
        )
    }
}


