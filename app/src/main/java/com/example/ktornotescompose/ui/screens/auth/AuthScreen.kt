package com.example.ktornotescompose.ui.screens.auth

import android.util.DisplayMetrics
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ktornotescompose.R
import com.example.ktornotescompose.ui.navigation.UiEvent
import com.example.ktornotescompose.ui.screens.auth.components.AuthHeader
import com.example.ktornotescompose.ui.screens.auth.components.ExistingAccount
import com.example.ktornotescompose.ui.screens.auth.components.LoginInfoBox
import com.example.ktornotescompose.ui.screens.auth.components.RegisterInfoBox

@Composable
fun AuthScreen(
    scaffoldState: ScaffoldState,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
        if (!state.isExpanded){
            ExistingAccount(
                viewModel = viewModel,
                modifier = Modifier
                    .fillMaxHeight(0.8f)
                    .fillMaxWidth()
            )
        }
        RegisterContainer(
            modifier = when (state.isExpanded) {
                false -> {
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.4f)
                        .background(Color.LightGray)
                }
                true -> {
                    Modifier
                        .fillMaxSize()
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

@Composable
fun RegisterContainer(
    modifier: Modifier = Modifier,
    emailChanged: (String) -> Unit,
    passwordChanged: (String) -> Unit,
    confirmPasswordChanged: (String) -> Unit,
    onRegisterClick: () -> Unit,
    isExpanded: Boolean = false,
    expandClicked: (Boolean) -> Unit,
    email: String,
    password: String,
    confirmPassword: String,
) {
        Column(
            modifier,
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row (
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = when (isExpanded) {
                        true -> stringResource(id = R.string.already_has_account)
                        false -> stringResource(id = R.string.register_account)
                    },
                    color = Color.White,
                    modifier = Modifier.padding(start = 12.dp)
                )
                Icon(
                    imageVector = when (isExpanded) {
                        true -> Icons.Default.KeyboardArrowDown
                        false -> Icons.Default.KeyboardArrowUp
                    },
                    contentDescription = null,
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            expandClicked(!isExpanded)
                        }
                )
            }
            if (isExpanded) {
                RegisterInfoBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize()
                        .padding(20.dp),
                    email = email,
                    password = password,
                    confirmPassword = confirmPassword,
                    emailChanged = { email ->
                        emailChanged(email)
                    },
                    passwordChanged = { password ->
                        passwordChanged(password)
                    },
                    confirmPasswordChanged = {
                        confirmPasswordChanged(it)
                    }
                )
                Button(
                    onClick = {
                        onRegisterClick()
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Cyan),
                    modifier = Modifier
                        .wrapContentSize()
                ) {
                    Text(text = stringResource(id = R.string.register))
                }
            }
        }
}
