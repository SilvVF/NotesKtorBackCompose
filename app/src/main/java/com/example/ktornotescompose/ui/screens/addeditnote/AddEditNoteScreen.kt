package com.example.ktornotescompose.ui.screens.addeditnote

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ktornotescompose.R

@Composable
fun AddEditNoteScreen(
    scaffoldState: ScaffoldState,
    viewModel: AddEditNoteViewModel = hiltViewModel()
) {
    val state = viewModel.state

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f)
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
            Canvas(modifier = Modifier.wrapContentSize()) {
                drawCircle(
                    color = state.color,
                    radius = 30f,
                )
            }
        }
        Box(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f)) {
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
            )
        }
    }
}