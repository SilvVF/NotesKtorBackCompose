package com.example.ktornotescompose.ui.screens.addeditnote

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ktornotescompose.R
import com.example.ktornotescompose.ui.dialog.ColorPickerDialog
import com.example.ktornotescompose.ui.navigation.UiEvent
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

@Composable
fun AddEditNoteScreen(
    scaffoldState: ScaffoldState,
    noteId: String,
    viewModel: AddEditNoteViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val context = LocalContext.current
    val colorController = rememberColorPickerController()
    DisposableEffect(key1 = viewModel) {
        onDispose { viewModel.saveNote() }
    }
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is UiEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        uiEvent.message.asString(context)
                    )
                }
                else -> {}
            }
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.subscribeToNoteUpdates()
    }
    LaunchedEffect(key1 = true) {
        viewModel.getNoteById(noteId)
    }
    if (state.isColorPickerDisplayed) {
        ColorPickerDialog(
            controller = colorController,
            onColorChange = { colorEnvelope ->
                viewModel.onEvent(AddEditNoteEvent.OnColorChanged(colorEnvelope.color))
            },
            onDismiss = {
                viewModel.onEvent(AddEditNoteEvent.OnDialogDismissed)
            },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .fillMaxHeight(0.5f)
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            TextField(
                value = state.titleText,
                onValueChange = {
                    viewModel.onEvent(AddEditNoteEvent.OnTitleTextChange(it))
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.title)
                    )
                },
                modifier = Modifier.fillMaxWidth(0.8f),
            )
            Canvas(
                modifier = Modifier
                    .wrapContentSize()
                    .align(CenterVertically)
                    .offset(x = 40.dp)
                    .clickable {
                        viewModel.onEvent(AddEditNoteEvent.OnColorChangeClick)
                    }
            ) {
                drawCircle(
                    color = state.color,
                    radius = 70f,
                )
            }
        }
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            TextField(
                value = state.contentText,
                onValueChange = {
                    viewModel.onEvent(AddEditNoteEvent.OnNoteContentTextChange(it))
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.content)
                    )
                },
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
            )
        }
    }
}