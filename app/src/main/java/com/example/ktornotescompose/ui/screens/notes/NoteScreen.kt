package com.example.ktornotescompose.ui.screens.notes

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.InspectableModifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ktornotescompose.R
import com.example.ktornotescompose.data.local.entities.Note
import com.example.ktornotescompose.ui.navigation.Routes
import com.example.ktornotescompose.ui.navigation.UiEvent
import com.example.ktornotescompose.ui.screens.notes.components.NoteItem
import com.example.ktornotescompose.ui.screens.notes.components.TopBar
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun NoteScreen(
    scaffoldState: ScaffoldState,
    viewModel: NoteViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit,
    onNoteClicked: (String) -> Unit
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
    LaunchedEffect(key1 = true){
        viewModel.subscribeToNotes()
    }
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = state.isRefreshing),
        onRefresh = { /*TODO*/ }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
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
            Text(
                text = stringResource(id = R.string.all_notes),
                color = Color.White,
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.86f)
            ) {
                items(state.notesList) {
                    NoteItem(
                        note = it,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .shadow(
                                elevation = 2.dp,
                                shape = RoundedCornerShape(
                                    topEnd = 7.dp,
                                    bottomEnd = 7.dp
                                )
                            )
                            .clip(
                                RoundedCornerShape(
                                    topEnd = 8.dp,
                                    bottomEnd = 8.dp
                                )
                            )
                            .height(80.dp)
                            .fillMaxWidth()
                    ) {
                       onNoteClicked(it.id)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            FloatingActionButton(
                onClick = {

                },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.End)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    }
}