package com.example.ktornotescompose.ui.screens.notes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.InspectableModifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ktornotescompose.R
import com.example.ktornotescompose.ui.navigation.Routes
import com.example.ktornotescompose.ui.navigation.UiEvent
import com.example.ktornotescompose.ui.screens.notes.components.TopBar
import org.intellij.lang.annotations.JdkConstants

@Composable
fun NoteScreen(
    scaffoldState: ScaffoldState,
    viewModel: NoteViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit
) {
    val state = viewModel.state
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.NavigateUp -> {}
                is UiEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message.asString(context)
                    )
                }
                else -> {}
            }
        }
    }
    Column(Modifier.fillMaxSize()) {
        TopBar(
            isMenuExpanded = state.isMenuDisplayed,
            onMenuIconClick = {
                viewModel.onEvent(NoteScreenEvent.OnMenuButtonClick)
            },
            onLogoutClick = {
                viewModel.logout()
                onNavigate(Routes.AUTH_ROUTE)
            }
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = stringResource(id = R.string.all_notes),
                color = Color.White,
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .fillMaxHeight(0.2f)
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(0.8f)
            ) {

            }
            FloatingActionButton(
                onClick = {

                },
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(16.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    }
}