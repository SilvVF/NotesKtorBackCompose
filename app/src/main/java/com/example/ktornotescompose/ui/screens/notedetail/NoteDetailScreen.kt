package com.example.ktornotescompose.ui.screens.notedetail

import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun NoteDetailScreen(
    noteID: String,
    scaffoldState: ScaffoldState,
    viewModel: NoteDetailViewModel = hiltViewModel()
) {
    Text(text = noteID.toString())
}