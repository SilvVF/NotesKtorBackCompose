package com.example.ktornotescompose.ui.screens.notedetail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.InspectableModifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ktornotescompose.R
import com.example.ktornotescompose.ui.screens.addeditnote.AddEditNoteEvent
import com.example.ktornotescompose.ui.screens.notes.components.HexToJetpackColor
import java.nio.file.WatchEvent

@Composable
fun NoteDetailScreen(
    noteID: String,
    navigateToEdit:(String) -> Unit,
    scaffoldState: ScaffoldState,
    viewModel: NoteDetailViewModel = hiltViewModel()
) {
    val state = viewModel.state
    LaunchedEffect(key1 = true) {
        viewModel.getNoteFromDb(noteID)
    }
    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = state.note.title,
                    fontSize = 42.sp,
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .border(
                            BorderStroke(
                                width = 1.dp,
                                color = Color.Black
                            )
                        )
                        .padding(8.dp),
                )

                Canvas(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    drawCircle(
                        color = HexToJetpackColor.getColor(state.note.color),
                        radius = 90f,
                    )
                }
            }
            Box(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                Text(
                    text = state.note.content
                )
            }
        }
        FloatingActionButton(
            onClick = { navigateToEdit(noteID) },
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
        ) {
            Icon(
                imageVector = Icons.Default.Edit ,
                contentDescription = null,
                tint = Color.White
            )
        }
    }

}