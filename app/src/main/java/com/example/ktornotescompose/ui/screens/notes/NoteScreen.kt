package com.example.ktornotescompose.ui.screens.notes

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.InspectableModifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ktornotescompose.R
import com.example.ktornotescompose.data.local.entities.Note
import com.example.ktornotescompose.ui.navigation.Routes
import com.example.ktornotescompose.ui.navigation.UiEvent
import com.example.ktornotescompose.ui.screens.notes.components.NoteItem
import com.example.ktornotescompose.ui.screens.notes.components.TopBar
import com.example.ktornotescompose.ui.screens.notes.components.setUpRow
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Math.random
import java.util.*
import kotlin.random.Random

@OptIn(ExperimentalMaterialApi::class)
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
                is UiEvent.NavigateUp -> {
                    onNavigate(Routes.ADD_EDIT_NOTE_ROUTE + "id")
                }
                is UiEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message.asString(context),
                    )
                }
                is UiEvent.ShowSnackBarWithUndo -> {
                    val res = scaffoldState.snackbarHostState.showSnackbar(
                        message = "deleted",
                        actionLabel = "Undo"
                    )
                    when (res) {
                        SnackbarResult.ActionPerformed -> {
                            viewModel.undoDeletion(event.data)
                        }
                        else -> { }
                    }
                }
            }
        }
    }
    val owner =  LocalLifecycleOwner.current
    LaunchedEffect(key1 = true) {
        viewModel.subscribeToNotes()
    }
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = state.isRefreshing),
        onRefresh = { viewModel.onEvent(NoteScreenEvent.OnRefreshTrigger) }
    ) {
        Box {
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
                        .fillMaxHeight()
                ) {
                    items(
                       items = state.notesList,
                       key =  { noteItem: Note -> noteItem.id }
                    ){ item ->
                        val dismissState = rememberDismissState(
                            confirmStateChange = {
                                if (it == DismissValue.DismissedToStart) {
                                    viewModel.onEvent(NoteScreenEvent.OnDismissNoteTrigger(item))
                                }
                                true
                            }
                        )
                        SwipeToDismiss(
                            state = dismissState,
                            modifier = Modifier
                                .padding(vertical = Dp(1f)),
                            directions = setOf(
                                DismissDirection.EndToStart
                            ),
                            dismissThresholds = { direction ->
                                FractionalThreshold(if (direction == DismissDirection.EndToStart) 0.1f else 0.05f)
                            },
                            background = {
                                val color by animateColorAsState(
                                    when (dismissState.targetValue) {
                                        DismissValue.Default -> MaterialTheme.colors.background
                                        else -> Color.Red
                                    }
                                )
                                val alignment = Alignment.CenterEnd
                                val icon = Icons.Default.Delete

                                val scale by animateFloatAsState(
                                    if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
                                )

                                Box(
                                    Modifier
                                        .fillMaxSize()
                                        .background(color)
                                        .padding(horizontal = Dp(20f)),
                                    contentAlignment = alignment
                                ) {
                                    Icon(
                                        icon,
                                        contentDescription = "Delete Icon",
                                        modifier = Modifier.scale(scale)
                                    )
                                }
                            }
                        ) {
                            Card(
                                elevation = animateDpAsState(
                                    if (dismissState.dismissDirection != null) 4.dp else 0.dp
                                ).value,
                                modifier = Modifier
                                    .padding(end = 16.dp)
                                    .fillMaxWidth()
                                    .height(Dp(90f))
                                    .clip(RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp))
                                    .align(alignment = Alignment.CenterVertically)
                                    .shadow(
                                        elevation = 2.dp,
                                        RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp)
                                    )
                            ) {
                                setUpRow(item = item) {
                                    onNoteClicked(item.id)
                                }
                            }
                            Spacer(
                                Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                            )
                        }
                    }
                }
            }
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(NoteScreenEvent.OnFabButtonClick)
                },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomEnd)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    }
}