package com.example.ktornotescompose.ui.screens.notedetail

import android.provider.ContactsContract
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
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.InspectableModifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ktornotescompose.R
import com.example.ktornotescompose.ui.navigation.UiEvent
import com.example.ktornotescompose.ui.screens.addeditnote.AddEditNoteEvent
import com.example.ktornotescompose.ui.screens.notes.components.HexToJetpackColor
import com.example.ktornotescompose.ui.screens.notes.components.TopBar
import dev.jeziellago.compose.markdowntext.MarkdownText
import kotlinx.coroutines.flow.collect
import java.nio.file.WatchEvent

@Composable
fun NoteDetailScreen(
    noteID: String,
    navigateToEdit:(String) -> Unit,
    scaffoldState: ScaffoldState,
    viewModel: NoteDetailViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state = viewModel.state
    LaunchedEffect(key1 = true) {
        viewModel.getNoteFromDb(noteID)
    }
    LaunchedEffect(key1 = true ) {
        viewModel.subscribeToAddOwnersStatus()
    }
    LaunchedEffect(key1 = true){
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
    Box {
            Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                AddOwnerBar(
                    onAddOwnerIconClick = {viewModel.onEvent(NoteDetailEvent.OnAddOwnersIconClick)},
                )
                if (state.isAddOwnerDialogDisplayed) {
                    AddOwnerDialog(
                        onDismissRequest = { viewModel.onEvent(NoteDetailEvent.OnAddOwnersIconClick)},
                        text = state.ownersText,
                        onTextChange = { viewModel.onEvent(NoteDetailEvent.UpdateOwnerText(it)) },
                        onConfirmClick = { viewModel.onEvent(NoteDetailEvent.OnConfirmOwnersClick) }
                    )
                }

                Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MarkdownText(
                            markdown = state.note.title,
                            fontSize = 42.sp,
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .padding(8.dp),
                        )
                        Canvas(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                                .offset(y = 5.dp)
                                .wrapContentHeight()
                        ) {
                            drawCircle(
                                color = Color.Black,
                                radius = 95f,
                            )
                            drawCircle(
                                color = HexToJetpackColor.getColor(state.note.color),
                                radius = 90f,
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(
                        Modifier
                            .padding(4.dp)
                            .height(1.dp)
                            .fillMaxWidth(0.7f)
                            .border(1.dp, Color.Black)
                            .padding(4.dp)
                    ) {}
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        contentAlignment = Alignment.TopStart
                    ) {
                        MarkdownText(
                            markdown = """
                        ${state.note.content}
                    """,
                            color = Color.White,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }
                FloatingActionButton(
                    onClick = { navigateToEdit(noteID) },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit ,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
}