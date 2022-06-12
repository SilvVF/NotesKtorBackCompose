package com.example.ktornotescompose.ui.screens.auth.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.ktornotescompose.R

@OptIn(ExperimentalComposeUiApi::class)
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
        val kc = LocalSoftwareKeyboardController.current
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
                    kc?.hide()
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